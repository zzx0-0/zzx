package com.example.mnnu.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.mnnu.config.Constant;
import com.example.mnnu.dao.*;
import com.example.mnnu.dto.Judge;
import com.example.mnnu.enums.ProblemTypeEnum;
import com.example.mnnu.enums.ResponseEnum;
import com.example.mnnu.pojo.*;
import com.example.mnnu.service.IScoreService;
import com.example.mnnu.service.IUserService;
import com.example.mnnu.service.MQService;
import com.example.mnnu.service.WebSocket;
import com.example.mnnu.utils.MathUtil;
import com.example.mnnu.utils.Util;
import com.example.mnnu.vo.ProblemVO;
import com.example.mnnu.vo.ResponseVO;
import com.example.mnnu.vo.ExamVO;
import com.example.mnnu.vo.ScoreVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ScoreServiceImpl implements IScoreService {

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private JudgeOneMapper judgeOneMapper;

    @Autowired
    private IUserService userService;

    @Autowired
    private WebSocket webSocket;

    @Autowired
    private MQService mqService;

    @Override
    public ResponseVO sub(Judge judge) {
        Exam exam = examMapper.selectById(judge.getExamId());
        if (exam == null) {
            return ResponseVO.error(ResponseEnum.OTHERS_ERROR, "不存在该考试");
        }
        Score score = scoreMapper.getScore(judge.getUserCode(), judge.getExamId());
        if (score != null) {
            ExamVO examVO = new ExamVO();
            examVO.setScore(score.getScore());
            examVO.setSubList(Util.StringChangeToList(score.getSubmitAnswer(), String.class));
            examVO.setProblemVOList(getProblemVOList(exam));
            return ResponseVO.success(examVO);
        }
        // 检查
        int count = exam.getExamProCount();
        if (Util.StringChangeToList(judge.getSubList(), String.class).size() != count) {
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "该次考试共有"+ count +"题，请检查后提交。");
        }

        mqService.send(Constant.EXAM_JUDGE, judge);
        log.info("【发送MQ消息】=> {}", JSON.toJSONString(judge));
        return ResponseVO.ff(ResponseEnum.SUBMIT_SUCCESS);
    }

    @Override
    public ResponseVO score(Long examId, String userCode) {
        Score score = scoreMapper.getScore(userCode, examId);
        if (score == null)
            return ResponseVO.ff(ResponseEnum.SUBMIT_SUCCESS, "还未有该次成绩");
        return ResponseVO.success(score.getScore());
    }

    @Override
    public List<ProblemVO> getProblemVOList(Exam exam) {
        String problemIds = exam.getExamProblemId();
        List problemIdList = Util.StringChangeToList(problemIds, Long.class);
        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("problem_id", problemIdList);
        return problemMapper.selectProblemVO(queryWrapper);
    }

    @Override
    public void judge(Judge judge) {
        log.info("Judge= {}", judge);
        Exam exam = examMapper.selectById(judge.getExamId());
        log.info("{}提交的试卷{}，作答 ={}", judge.getUserCode(), judge.getExamId(), judge.getSubList());     // 这是提交的答案

        String problemIds = exam.getExamProblemId();
        List proIdList = Util.StringChangeToList(problemIds, Long.class);
        log.info("problemIdList={}",proIdList);// 题目列表

        List<String> subList = Util.StringChangeToList(judge.getSubList(), String.class);
        if (proIdList.size() != subList.size())
            throw new RuntimeException("该次考试共有"+ proIdList.size() +"题，该生回答" + subList.size()+"题。");
        Integer[][] pro = Util.StringChangeToIntList(exam.getExamPro(), true);

        Score score = new Score(judge.getUserCode(), judge.getExamId(), judge.getSubList());

        BigDecimal s = new BigDecimal("0");

        int ti = 0;  //第几小题
        for (int i=1; i<=5; i++) {   //第几大题
            for (int j = 0; j < pro[i - 1][0]; j++) {

                int per_s = pro[i - 1][1];   // 该大题下每小题的分值；

                JudgeOne judgeOne = new JudgeOne();
                judgeOne.setUserCode(judge.getUserCode());
                judgeOne.setExamId(judge.getExamId());
                judgeOne.setProblemId((Long) proIdList.get(ti));
                judgeOne.setSub(subList.get(ti));
                judgeOne.setScore(new BigDecimal(per_s));

                s = s.add(judgeOne(judgeOne, i));

                ti++;
            }
        }

        score.setScore(s);
        scoreMapper.insert(score);
    }

    /**
     * 对于单题程序无法自动判断者，对提交为“ERROR”的先给0分，后期再加分；
     * 其余（书写题）给满分，后期减分。
     */
    @Override
    public BigDecimal judgeOne(JudgeOne judgeOne, Integer type) {
        log.info("proId={}, 提交的答案为={}", judgeOne.getProblemId(), judgeOne.getSub());
        if (judgeOne.getSub().equals(Constant.ERROR)) {
            return needJudgeByTeacher(judgeOne, new BigDecimal("0"));
        }
        if (type != null && type == 5) {
            return needJudgeByTeacher(judgeOne, judgeOne.getScore());
        }

        Problem pro = problemMapper.selectById(judgeOne.getProblemId());
        if (pro.getProblemType().equals(ProblemTypeEnum.WRITE.getCode())) {
            return needJudgeByTeacher(judgeOne, judgeOne.getScore());
        }

        if (pro.getProblemAnswer().equals(judgeOne.getSub().trim())) {
            return judgeOne.getScore();
        } else {
            log.info("正确答案={}, 提交答案={}。 此题0分", pro.getProblemAnswer(), judgeOne.getSub());
            return new BigDecimal("0");
        }
    }

                                                           // 这里的分值是暂给几分
    private BigDecimal needJudgeByTeacher(JudgeOne judgeOne, BigDecimal decimal) {
        JudgeOne Jo = judgeOneMapper.findOne(judgeOne.getUserCode(), judgeOne.getExamId(), judgeOne.getProblemId());
        if (Jo == null) {
            judgeOne.setGive(decimal);
            judgeOne.setJudged(0);
            Util.ff(judgeOneMapper.insert(judgeOne));
            log.info("【发送webSocket消息】");
            webSocket.sendMessage("有新问题啦， 改卷");
            return judgeOne.getGive();
        } else {
            return Jo.getGive();
        }
    }

    public ResponseVO getUserAllScore(String userCode) {
        User user = userService.findByUserCode(userCode);
        List<Score> scoreList = scoreMapper.getUserAllScore(userCode);
        if (scoreList.size() == 0){
            ScoreVO scoreVO = new ScoreVO();
            scoreVO.setCount(0);
            scoreVO.setScList(new ArrayList<>(Collections.nCopies(1, new BigDecimal("0"))));
            scoreVO.setMessage("‘" + user.getUserName() + "’无战绩");
            return ResponseVO.success(scoreVO);
        }
        ScoreVO scoreVO = new ScoreVO();
        for (Score s : scoreList) {
            s.setRemark(examMapper.selectById(s.getExamId()).getExamName());
        }
        scoreVO.setScoreList(scoreList);
        scoreVO.setCount(scoreList.size());

        List<BigDecimal> scList = new ArrayList<>();
        List<Long> eList = new ArrayList<>();
        for (Score s : scoreList) {
            scList.add(s.getScore());
            eList.add(s.getExamId());
        }
        scoreVO.setScList(scList);
        scoreVO.setEList(eList);
        scoreVO.setMessage("‘" +user.getUserName() + "’整体成绩信息");
        return ResponseVO.success(scoreVO);
    }

    public ResponseVO getExamAllScore(Long examId) {
        Exam exam = examMapper.selectById(examId);
        if (exam == null)
            return ResponseVO.error(ResponseEnum.OTHERS_ERROR, "试卷ID有误");
        List<Score> scoreList = scoreMapper.getExamAllScore(examId);
        if (scoreList.size() == 0) {
            ScoreVO scoreVO = new ScoreVO();
            scoreVO.setCount(0);
            scoreVO.setNumList(new ArrayList<Integer>(Collections.nCopies(10, 0)));
            scoreVO.setMessage("‘" + exam.getExamName() + "’无成绩");
            return ResponseVO.success(scoreVO);
        }
        ScoreVO scoreVO = new ScoreVO();
        for (Score s : scoreList) {
            s.setRemark(userService.findByUserCode(s.getUserCode()).getUserName());
        }
        scoreVO.setScoreList(scoreList);
        scoreVO.setCount(scoreList.size());

        List<Integer> numList = new ArrayList<Integer>(Collections.nCopies(10, 0));
        for (Score s : scoreList) {
            int l = MathUtil.to(s.getScore());
            numList.set(l, numList.get(l)+1);

        }
        scoreVO.setNumList(numList);
        scoreVO.setMessage("‘" +exam.getExamName() + "’整体成绩信息");
        return ResponseVO.success(scoreVO);
    }

    @Override
    public ResponseVO<IPage> getJudgeOnes(String problemId, Integer pageNum, Integer pageSize) {
     //   PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<JudgeOne> queryWrapper = new QueryWrapper<>();
    //    queryWrapper.in("judged", status);
        if (StringUtils.isNotEmpty(problemId)) {
            queryWrapper.in("problem_id",problemId);
        }
        queryWrapper.orderByAsc("judged");
        List<JudgeOne> allJudgeOne = judgeOneMapper.selectList(queryWrapper);
        return ResponseVO.success(Util.pageInfo(allJudgeOne));
    }

    @Override
    public ResponseVO getJO(Integer judgeId) {
        JudgeOne judgeOne = judgeOneMapper.selectById(judgeId);
        if (judgeOne == null)
            throw  new RuntimeException("jId有误");
        Problem problem = problemMapper.selectById(judgeOne.getProblemId());
        judgeOne.setProblemText(problem.getProblemText());
        judgeOne.setProblemAnswer(problem.getProblemAnswer());
        return ResponseVO.success(judgeOne);
    }

    @Override
    public ResponseVO updateScore(Long judgeId, BigDecimal newScore, String userCode) {
        JudgeOne judgeOne = judgeOneMapper.selectById(judgeId);
        if (judgeOne == null)
            throw new RuntimeException("jId不存在");
        if (newScore.compareTo(judgeOne.getScore()) > 0 || newScore.compareTo(new BigDecimal("0")) < 0)
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "给分有问题");
        BigDecimal oldScore = judgeOne.getGive();
        judgeOne.setGive(newScore);
        judgeOne.setJudged(1);
        judgeOne.setJudgeBy(userService.findByUserCode(userCode).getUserName());
        judgeOneMapper.updateById(judgeOne);

        Score score = scoreMapper.getScore(judgeOne.getUserCode(), judgeOne.getExamId());
        score.setScore(score.getScore().subtract(oldScore).add(newScore));   //score.getScore() - oldScore + newScore
        score.setRemark("");
        scoreMapper.updateById(score);
        return ResponseVO.success();
    }

    @Override
    public ResponseVO que(String userCode, Long examId, Long proId, BigDecimal yuanScore) {
        JudgeOne Jo = judgeOneMapper.findOne(userCode,examId,proId);
        if (Jo != null) {
            return ResponseVO.success("此题已经老师改过，无误。若有进一步需要，请联系老师");
        }
        JudgeOne judgeOne = new JudgeOne();
        judgeOne.setUserCode(userCode);
        judgeOne.setExamId(examId);
        judgeOne.setProblemId(proId);
        judgeOne.setGive(yuanScore);
        judgeOne.setJudged(0);

        Exam exam = examMapper.selectById(examId);
        Integer i = MathUtil.findStringIn(Util.StringChangeToList(exam.getExamProblemId(),Long.class), proId.toString());

        Integer[][] integers = Util.StringChangeToIntList(exam.getExamPro(),true);
        judgeOne.setScore(BigDecimal.valueOf(Util.findScore(integers, i)));

        Score sc = scoreMapper.getScore(userCode,examId);
        List list = Util.StringChangeToList(sc.getSubmitAnswer(), String.class);
        judgeOne.setSub((String) list.get(i));

        Util.ff(judgeOneMapper.insert(judgeOne));

        return ResponseVO.success();
    }

}

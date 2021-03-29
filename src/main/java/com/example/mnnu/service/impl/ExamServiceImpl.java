package com.example.mnnu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.mnnu.dao.ExamMapper;
import com.example.mnnu.dao.ProblemMapper;
import com.example.mnnu.dto.ProblemText;
import com.example.mnnu.enums.ProblemTypeEnum;
import com.example.mnnu.enums.ResponseEnum;
import com.example.mnnu.form.ExamSaveForm;
import com.example.mnnu.pojo.Exam;
import com.example.mnnu.pojo.Problem;
import com.example.mnnu.service.IExamService;
import com.example.mnnu.service.IProblemService;
import com.example.mnnu.utils.MathUtil;
import com.example.mnnu.utils.TimeUtil;
import com.example.mnnu.utils.Util;
import com.example.mnnu.vo.ResponseVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class ExamServiceImpl implements IExamService {

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private IProblemService problemService;

    public Exam changeToExam(ExamSaveForm examSaveForm) {
        LocalDateTime beginTime = TimeUtil.changeTimePattern(examSaveForm.getExamBeginTime());
        LocalDateTime endTime = TimeUtil.changeTimePattern(examSaveForm.getExamEndTime());

        Duration duration = Duration.between(beginTime, endTime);
        if (duration.toMinutes() <= 0)
            throw new RuntimeException("时间先后有问题");
        long hours = duration.toHours();
        long minutes = duration.toMinutes() - hours * 60;
        BigDecimal length = new BigDecimal(minutes).divide(new BigDecimal("60"), 2, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(hours));

        Exam exam = new Exam();
        BeanUtils.copyProperties(examSaveForm, exam);
        exam.setExamBeginTime(beginTime);
        exam.setExamEndTime(endTime);
        exam.setExamLength(length);
        if (examSaveForm.getExamId() != null)
            exam.setExamId(examSaveForm.getExamId());
        return exam;
    }

    private static Integer legal(Integer[][] list) {
        int a = 0, count = 0;
        for (Integer[] integers : list) {
            a = a + integers[0] * integers[1];
            count = count + integers[0];
        }
        if (a != 100)
            throw new RuntimeException("试卷总分不为100");
        return count;
    }

    private Exam save(Exam exam) {
        Integer[][] list = Util.StringChangeToIntList(exam.getExamPro(), true);
        int count = legal(list);

        if (exam.getExamProblemId() == null || exam.getExamProblemId().equals("")) {
            List examProId = new ArrayList();
            for (int i=1; i<=5; i++){
                List proIdList = problemService.getProIdByType(i);
                int num = list[i-1][0];
                List getProIdList = MathUtil.getRandomList(proIdList, num, ProblemTypeEnum.getEnum(i));
                log.info("{} 试题为 {}", ProblemTypeEnum.getEnum(i).getDesc(), getProIdList);
                examProId.addAll(getProIdList);
            }

            exam.setExamProblemId(examProId.toString());
        } else {
            if (Util.StringChangeToList(exam.getExamProblemId(), Long.class).size() != count ){
                throw new RuntimeException("题目列表与分数表不符");
            }
        }
        exam.setExamProCount(count);
        return exam;
    }

    @Override
    public ResponseVO add(Exam exam) {
        return Util.ff(examMapper.insert(save(exam)));
    }

    @Override
    public ResponseVO upd(Exam exam) {
        if (exam.getExamBeginTime().isBefore(LocalDateTime.now()))
            return ResponseVO.error(ResponseEnum.EXAM_ERROR, "已经开始考试了，不能修改试卷");
        exam = save(exam);
        return Util.ff(examMapper.updateById(exam));
    }

    @Override
    public ResponseVO delete(Long examId) {
        Exam exam = examMapper.selectById(examId);
        if (exam == null) {
            return ResponseVO.error(ResponseEnum.OTHERS_ERROR, "试卷ID有误");
        }
        if (exam.getExamBeginTime().isBefore(LocalDateTime.now())) {
            return ResponseVO.error(ResponseEnum.OTHERS_ERROR, "不能删除已经开始考试了的试卷");
        }
        return Util.ff(examMapper.deleteById(examId));
    }

    @Override
    public ResponseVO show(Integer pageNum, Integer pageSize) {
    //    return ResponseVO.success(examMapper.selectAll());
        PageHelper.startPage(pageNum, pageSize);
        List<Exam> exams = examMapper.selectAll();
        return ResponseVO.success(Util.pageInfo(exams));

//        PageVO pageVO = new PageVO();
//        pageVO.setCount(exams.size());
//        pageVO.setDataList(exams);
//        return ResponseVO.success(pageVO);
    }

    @Override
    public ResponseVO detail(Long examId) {
        Exam exam = examMapper.selectById(examId);
        if (exam == null) {
            throw new RuntimeException("试卷ID有误");
        }
        return ResponseVO.success(exam);
    }

    @Override
    public ResponseVO<PageInfo> enter(Long examId, String examPassword, Integer pageNum, Integer pageSize) {
        Exam exam = examMapper.selectById(examId);
        if (exam == null)
            return ResponseVO.error(ResponseEnum.OTHERS_ERROR, "不存在该考试");
        if (!exam.getExamPassword().equals(examPassword)) {
            return ResponseVO.ff(ResponseEnum.PASSWORD_ERROR);
        }
        if (exam.getExamBeginTime().isAfter(LocalDateTime.now()))
            return ResponseVO.error(ResponseEnum.ERROR_TIME,"考试还未开始");
        if (exam.getExamEndTime().isBefore(LocalDateTime.now()))
            return ResponseVO.error(ResponseEnum.ERROR_TIME,"考试已结束");

        String problemIds = exam.getExamProblemId();
        List problemIdList = Util.StringChangeToList(problemIds, Long.class);
        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("problem_id", problemIdList);

        PageHelper.startPage(pageNum, pageSize);
        List<ProblemText> problemTextList = problemMapper.selectProblemText(queryWrapper);
        return ResponseVO.success(Util.pageInfo(problemTextList));
    }

}

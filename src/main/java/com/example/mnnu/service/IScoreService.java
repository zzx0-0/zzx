package com.example.mnnu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.mnnu.dto.Judge;
import com.example.mnnu.pojo.Exam;
import com.example.mnnu.pojo.JudgeOne;
import com.example.mnnu.vo.ProblemVO;
import com.example.mnnu.vo.ResponseVO;

import java.math.BigDecimal;
import java.util.List;

public interface IScoreService {

    ResponseVO sub(Judge judge);

    List<ProblemVO> getProblemVOList(Exam exam);

    void judge(Judge judge);

    BigDecimal judgeOne(JudgeOne judgeOne, Integer type);

    ResponseVO getUserAllScore(String userCode);

    ResponseVO getExamAllScore(Long examId);

    ResponseVO score(Long examId, String userCode);

    ResponseVO<IPage> getJudgeOnes(String problemId, Integer pageNum, Integer pageSize);

    ResponseVO getJO(Integer judgeId);

    ResponseVO updateScore(Long judgeId, BigDecimal score, String userCode);

    ResponseVO que(String userCode, Long examId, Long proId, BigDecimal score);

}

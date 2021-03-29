package com.example.mnnu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mnnu.pojo.JudgeOne;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface JudgeOneMapper extends BaseMapper<JudgeOne> {

    @Select("select * from judge_one order by judged")
    List<JudgeOne> getAllJudgeOne();

    @Select("select * from judge_one where user_code = #{userCode} and exam_id = #{examId} and problem_id = #{proId}")
    JudgeOne findOne(String userCode, Long examId, Long proId);

}

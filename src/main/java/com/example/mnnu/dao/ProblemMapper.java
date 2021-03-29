package com.example.mnnu.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.example.mnnu.dto.ProblemText;
import com.example.mnnu.pojo.Problem;
import com.example.mnnu.vo.ProblemVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ProblemMapper extends BaseMapper<Problem> {

    List<Problem> selectByProblemIds(@Param("problemIdList") List problemIdList);

    @Select("select problem_text from problem ${ew.customSqlSegment}")
    List<ProblemText> selectProblemText(@Param(Constants.WRAPPER) Wrapper<Problem> wrapper);

    @Select("select problem_text, problem_answer, problem_type from problem ${ew.customSqlSegment}")
    List<ProblemVO> selectProblemVO(@Param(Constants.WRAPPER) Wrapper<Problem> wrapper);

    @Select("select problem_id from problem where problem_type = #{type}")
    List<Long> getAllProIdByType(Integer type);

    @Select("select * from problem")
    List<Problem> selectAll();

}

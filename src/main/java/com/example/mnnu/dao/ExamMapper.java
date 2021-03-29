package com.example.mnnu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mnnu.pojo.Exam;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ExamMapper extends BaseMapper<Exam> {

    @Select("select * from exam order by exam_begin_time desc")
    List<Exam> selectAll();
}

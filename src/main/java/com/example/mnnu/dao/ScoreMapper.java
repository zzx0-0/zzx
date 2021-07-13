package com.example.mnnu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mnnu.pojo.Score;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface ScoreMapper extends BaseMapper<Score> {

    @Select("select * from score where user_code = #{userCode}")
    List<Score> getUserAllScore(String userCode);

    @Select("select * from score where exam_id = #{examID}")
    List<Score> getExamAllScore(Long examID);

    @Select("select * from score where user_code = #{userCode} and exam_id = #{examID}")
    Score getScore(String userCode, Long examID);

    @Select("select exam_id, score from score group by exam_id")
    List<Score> getMaxScore();


//    @Select("SELECT\n" +
//            "\t`score`.`user_id`,\n" +
//            "\t`score`.`exam_id`,\n" +
//            "\t`score`.`score`\n" +
//            "FROM\n" +
//            "\t`score`,\n" +
//            "\t( SELECT `score`.`exam_id`, MAX( `score`.`score` ) AS `max_score` FROM `score` GROUP BY `score`.`exam_id` ) AS `fuck`\n" +
//            "WHERE\n" +
//            "\t`score`.`exam_id` = `fuck`.`exam_id`\n" +
//            "\tAND `score`.`score` = `fuck`.`max_score`")
    List<Score> getMax();


}

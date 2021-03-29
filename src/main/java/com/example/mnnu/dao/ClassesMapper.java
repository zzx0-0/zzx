package com.example.mnnu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mnnu.pojo.Classes;
import com.example.mnnu.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ClassesMapper extends BaseMapper<Classes> {

    @Select("select * from classes where class_code = #{classCode} ")
    Classes findByClassCode(@Param("classCode") String classCode);

    @Select("select user_id, user_Code, user_name, user_class_code, user_gender\n" +
            "from user\n" +
            "inner join classes\n" +
            "on classes.class_code = user.user_class_code\n" +
            "where classes.class_id = #{classId}")
    List<User> getClassPension(Long classId);

}

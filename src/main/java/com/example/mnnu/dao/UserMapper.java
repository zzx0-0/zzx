package com.example.mnnu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mnnu.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where user_code = #{userCode} and user_deleted = 0")
    User findByUserCode(@Param("userCode") String userCode);

    @Select("select * from user where user_code = #{userCode} ")
    User findByUserCodeAll(@Param("userCode") String userCode);

    @Select("select * from user where user_openid = #{openid}  and user_deleted = 0")
    User findByOpenId(String openid);

    @Select("select * from user where user_class_code = #{classCode} and user_deleted = 0 order by user_code")
    List<User> findByClassCode(String classCode);

}

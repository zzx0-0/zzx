package com.example.mnnu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.mnnu.form.UserForm;
import com.example.mnnu.pojo.User;
import com.example.mnnu.vo.ResponseVO;
import io.swagger.models.auth.In;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface IUserService {

    ResponseVO register(UserForm userRegisterForm);

    ResponseVO registerList(List<List> list);

    ResponseVO<User> login(String userCode, String userPassword);

    ResponseVO setInfo(User user);

    ResponseVO deleteUser(String value1, String userCode);

    User findByUserCode(String userCode);

    User findByUserCodeAll(String userCode);

    User findByOpenid(String openid);

    ResponseVO<User> bang(String openid, String uerCode) throws UnsupportedEncodingException;

    ResponseVO password(String uerCode, String oldPsw, String newPsw);

    ResponseVO setPsw(String value1, String userCode, String newPsw);

    ResponseVO setAvatar(String userCode, String imgUrl);

    ResponseVO<IPage> getUsers(String user, Integer role, Integer pageNum, Integer pageSize);

}

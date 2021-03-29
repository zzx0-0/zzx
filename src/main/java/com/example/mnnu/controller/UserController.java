package com.example.mnnu.controller;

import com.example.mnnu.enums.ResponseEnum;
import com.example.mnnu.form.UserUpdateForm;
import com.example.mnnu.pojo.User;
import com.example.mnnu.service.IUserService;
import com.example.mnnu.utils.Util;
import com.example.mnnu.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Api(value = "用户管理类 (要特别注意用户的逻辑删除)")
@RestController
public class UserController {

    @Autowired
    private IUserService userService;

    @PutMapping("/i/users")
    @ApiOperation(value = "设置个人基本信息")
    public ResponseVO setSelfInfo(@Valid @RequestBody UserUpdateForm userUpdateForm,
                                  HttpSession session) {
        String userCode = Util.getCurrentUserCode(session);
        if (!userUpdateForm.getUserCode().equals(userCode))
            return ResponseVO.error(ResponseEnum.NOT_ACCEPT, "不能替别人改资料！");
        User user = new User();
        BeanUtils.copyProperties(userUpdateForm, user);
        LocalDate bir = LocalDate.parse(userUpdateForm.getUserBirthday(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        user.setUserBirthday(bir);
        return userService.setInfo(user);
    }

    @PutMapping("/i/password")
    @ApiOperation(value = "修改密码")
    public ResponseVO password(@RequestParam String oldPsw,
                               @RequestParam String newPsw,
                               HttpSession session) {
        String userCode = Util.getCurrentUserCode(session);
        return userService.password(userCode, oldPsw, newPsw);
    }

    @PutMapping("/2/password")
    @ApiOperation(value = "管理员种植密码")
    public ResponseVO setPsw(@RequestParam String userCode,
                             @RequestParam String password,
                             @RequestParam String value1) {
        return userService.setPsw(value1, userCode, password);
    }

    @DeleteMapping("/2/users/{userCode}")
    @ApiOperation(value = "删除用户")
    public ResponseVO deleteUser(@PathVariable String userCode,
                                 @RequestParam String value1) {
        return userService.deleteUser(value1, userCode);
    }

    @GetMapping("/2/users")
    @ApiOperation(value = "获取用户")
    public ResponseVO getUsers(@RequestParam(required = false) String user,
                               @RequestParam(defaultValue = "-1") Integer role,
                               @RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "10") Integer pageSize) {
        return userService.getUsers(user, role, pageNum, pageSize);
    }

    @GetMapping("/i/users/{userCode}")
    @ApiOperation(value = "获取某个用户信息")
    public ResponseVO getUser(@PathVariable String userCode, HttpSession session) {
        User user;
        if (Util.getCurrentUserRole(session) == 2)
            user = userService.findByUserCodeAll(userCode);
        else
            user = userService.findByUserCode(userCode);
        return ResponseVO.success(user);
    }

}

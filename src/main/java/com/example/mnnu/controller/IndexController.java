package com.example.mnnu.controller;

import com.example.mnnu.config.Constant;
import com.example.mnnu.enums.ResponseEnum;
import com.example.mnnu.form.UserLoginForm;
import com.example.mnnu.form.UserForm;
import com.example.mnnu.pojo.User;
import com.example.mnnu.service.IUserService;
import com.example.mnnu.utils.AgentUserKit;
import com.example.mnnu.utils.CookieUtil;
import com.example.mnnu.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Api(value = "注册登录类")
@Controller
public class IndexController {

    @Autowired
    private IUserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/")
    @ApiOperation(value = "自动登录")
    public String auto(HttpServletRequest request, HttpServletResponse response) throws IOException, RedisConnectionFailureException {
        log.info("用户设备={}", AgentUserKit.getDeviceInfo(request));
        Cookie cookie = CookieUtil.get(request, Constant.TOKEN);
        if (cookie != null) {
            String value = redisTemplate.opsForValue().get(String.format(Constant.TOKEN_PREFIX, cookie.getValue()));
            if (value == null) {
                log.warn("有token， 但被redis删了");
                return "login";
            }
            int i = value.lastIndexOf('-');
            String code = value.substring(0, i);
            request.getSession().setAttribute(Constant.CURRENT_USER_CODE, code);
            i = value.charAt(i + 1) - '0';
            request.getSession().setAttribute(Constant.CURRENT_USER_ROLE, i);
            if (i == 0) response.sendRedirect("/0");
            else if (i == 1) response.sendRedirect("/1");
            else if (i == 2) response.sendRedirect("/2");
        } else {
            if (request.getSession().getAttribute(Constant.CURRENT_USER_CODE) != null
                && request.getSession().getAttribute(Constant.CURRENT_USER_ROLE) != null ) {
                int i = (int) request.getSession().getAttribute(Constant.CURRENT_USER_ROLE);
                if (i == 0) response.sendRedirect("/0");
                else if (i == 1) response.sendRedirect("/1");
                else if (i == 2) response.sendRedirect("/2");
            }
            else return "login";
        }
        return null;
    }

    @PostMapping("/login")
    @ResponseBody
    @ApiOperation(value = "登录")
    public ResponseVO login(@Valid @RequestBody UserLoginForm userLoginForm,
                            HttpServletRequest httpServletRequest, HttpServletResponse response) throws RedisConnectionFailureException {
        ResponseVO<User> userResponse = userService.login(userLoginForm.getUserCode(), userLoginForm.getUserPassword());
        if (userResponse.getCode() != 0) {
            return userResponse;
        }
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute(Constant.CURRENT_USER_CODE, userResponse.getData().getUserCode());
        session.setAttribute(Constant.CURRENT_USER_ROLE, userResponse.getData().getUserRole());

        log.info("Remember me= {}", userLoginForm.getRemember());
        if (userLoginForm.getRemember().equals("on")) {
            String token = UUID.randomUUID().toString();
            String value = userResponse.getData().getUserCode() + "-" + userResponse.getData().getUserRole();

            // 在用户浏览器设置Cookie，key为Constant.TOKEN("token"), value为一随机串，有限期Constant.EXPIRE(604800s, 即一周)
            CookieUtil.set(response, Constant.TOKEN, token, Constant.EXPIRE);

            // 在服务器Redis存储，key为Cookie的value， value为账号
            redisTemplate.opsForValue().set(String.format(Constant.TOKEN_PREFIX, token), value, Constant.EXPIRE, TimeUnit.SECONDS);
        }
        userResponse.setData(null);
        return userResponse;
    }

    @GetMapping("/loginByWx")
    @ApiOperation(value = "通过微信登录")
    public String loginByWx(@RequestParam String openId, HttpServletRequest request,
                            HttpServletResponse response) throws IOException {
        log.info("openid= {}",openId);

        User user;
        if (openId.contains("@")) {
            String code = openId.substring(openId.indexOf("@")+1);
            log.info("{}",code);
            openId = openId.substring(0, openId.indexOf("@"));
            ResponseVO<User> userResponse = userService.bang(openId, code);
            user = userResponse.getData();
            log.info("user={}",user);
        } else {
            user = userService.findByOpenid(openId);
            if (user == null) {
                throw new RemoteException("未绑定");
            }
        }

        HttpSession session = request.getSession();
        session.setAttribute(Constant.CURRENT_USER_CODE, user.getUserCode());
        session.setAttribute(Constant.CURRENT_USER_ROLE, user.getUserRole());
        response.sendRedirect("/");
        return null;
    }

    @PostMapping("/2/register")
    @ResponseBody
    @ApiOperation(value = "注册 / 更新")
    public ResponseVO register(@Valid @RequestBody UserForm userForm) {
        if (userForm.getUserId() != null) {
            User user = new User();
            BeanUtils.copyProperties(userForm, user);
            if (userForm.getUserDeleted() != null && userForm.getUserDeleted().equals("on")){
                if (userForm.getUserRole() == 0)
                    return ResponseVO.error(ResponseEnum.USER_NO_EXIT, "学生不可重新启用");
                else   user.setUserDeleted(0);   //重新启用
            }
            return userService.setInfo(user);
        }
        if (userForm.getUserPassword().trim().length() < 3)
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "密码太过简单");
        return userService.register(userForm);
    }

    @PostMapping("/2/registerList")
    @ResponseBody
    @ApiOperation(value = "批量注册")
    public ResponseVO registerList(@RequestBody List<List> list) {
        return userService.registerList(list);
    }

    @PostMapping("/logout")
    @ResponseBody
    @ApiOperation(value = "退出登录")
    public ResponseVO logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute(Constant.CURRENT_USER_CODE);
        request.getSession().removeAttribute(Constant.CURRENT_USER_ROLE);
        Cookie cookie = CookieUtil.get(request, Constant.TOKEN);
        if (cookie != null) {
            redisTemplate.opsForValue().getOperations().delete(String.format(Constant.TOKEN_PREFIX, cookie.getValue()));
            CookieUtil.set(response, Constant.TOKEN, null, 0);
        }
        return ResponseVO.success();
    }

    @GetMapping("/me")
    @ResponseBody
    @ApiOperation(value = "获取用户自身信息")
    public ResponseVO<User> userInfo(HttpSession session) {
        String currentUserCode = (String) session.getAttribute(Constant.CURRENT_USER_CODE);
        User currentUser = userService.findByUserCode(currentUserCode);
        log.info("currentUser={}", currentUser);
        return ResponseVO.success(currentUser);
    }

    @GetMapping("/myRole")
    @ResponseBody
    public ResponseVO<Integer> myRole(HttpSession session) {
        Integer role = (Integer) session.getAttribute(Constant.CURRENT_USER_ROLE);
        return ResponseVO.success(role);
    }

}
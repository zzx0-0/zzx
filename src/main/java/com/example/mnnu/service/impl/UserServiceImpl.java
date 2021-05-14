package com.example.mnnu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.mnnu.config.Constant;
import com.example.mnnu.dao.UserMapper;
import com.example.mnnu.form.UserForm;
import com.example.mnnu.pojo.User;
import com.example.mnnu.enums.ResponseEnum;
import com.example.mnnu.service.IUserService;
import com.example.mnnu.service.PushMsgService;
import com.example.mnnu.utils.Util;
import com.example.mnnu.vo.ResponseVO;
import com.github.pagehelper.PageHelper;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ClassesServiceImpl classesService;

    @Autowired
    private PushMsgService pushMsgService;

    @Override
    public ResponseVO register(UserForm userRegisterForm) {
        User user = userMapper.findByUserCodeAll(userRegisterForm.getUserCode());
        if (user != null) {
            if (user.getUserDeleted() == 0)
                return ResponseVO.error(ResponseEnum.USER_EXIT,userRegisterForm.getUserCode()+" 已存在");
            else
                return ResponseVO.error(ResponseEnum.USER_ONCE_EXIT,userRegisterForm.getUserCode()+" 曾被删除过");
        }
        user = new User();
        BeanUtils.copyProperties(userRegisterForm, user);
        user.setUserPassword(DigestUtils.md5DigestAsHex(user.getUserPassword().trim().getBytes(StandardCharsets.UTF_8)));
        if (user.getUserRole() == null || user.getUserRole() == 0) {
            if (user.getUserCode().length() != 10){
                return ResponseVO.error(ResponseEnum.PARAM_ERROR,user.getUserCode()+" 学生学号有误");
            }
            String classCode = user.getUserCode().substring(0, 8);
            classesService.addStu(classCode, user.getUserCode());
            user.setUserClassCode(classCode);
        }
        return Util.ff(userMapper.insert(user));
    }

    @Override
    public ResponseVO registerList(List<List> list) {
        final int first_line = 8;

        List errorMsg = new ArrayList();
        int succ = 0;
        int fail = 0;

        for (int i=0; i<list.size(); i++) {
            List one = list.get(i);
            UserForm form = new UserForm();
            String code = (String) one.get(0);
            String name = (String) one.get(1);
            String psw = (String) one.get(2);
            String gender = (String) one.get(3);
            String r = (String) one.get(4);

            if (StringUtils.isEmpty(code) || StringUtils.isEmpty(name) || StringUtils.isEmpty(psw)) {
            //    errorVO.add(ResponseVO.error(ResponseEnum.PARAM_ERROR, code + name + psw + "有空"));
                fail++;
                errorMsg.add("第"+ (i+first_line) +"行 关键字段有空  \n");
                continue;
            }
            if (!gender.equals("") && !gender.equals("男") && !gender.equals("女")) {
            //    errorVO.add(ResponseVO.error(ResponseEnum.PARAM_ERROR, code + " 性别只能为男/女"));
                fail++;
                errorMsg.add("第"+ (i+first_line) +"行 性别有误  ");
                continue;
            }
            int role = -1;
            if (r.equals("")) {
                role = 0;
            } else {
                try {
                    role = Integer.parseInt(r);
                } catch (NumberFormatException e) {
                    log.info("字符串'{}'转成数值失败", r);
                }
                if (role < 0 || role >2) {
                //    errorVO.add(ResponseVO.error(ResponseEnum.PARAM_ERROR,code +" 身份类型需为 0/1/2"));
                    fail++;
                    errorMsg.add("第"+ (i+first_line) +"行 角色有误  ");
                    continue;
                }
            }
            form.setUserCode(code);
            form.setUserName(name);
            form.setUserPassword(psw);
            form.setUserGender(gender);
            form.setUserRole(role);

            ResponseVO formVO = register(form);
            if (formVO.getCode() == 0)
                succ++;
            else {
                fail++;
                errorMsg.add("第" + (i + first_line) + "行 " + formVO.getMsg() + "  ");
            }
        }
        if (errorMsg.size() == 0){
            return ResponseVO.success(succ + "个账号全部成功");
        }
        return ResponseVO.ff(ResponseEnum.SUBMIT_SUCCESS, "成功"+succ+"个，失败"+fail+"个。 "+errorMsg.toString());
    }

    @Override
    public ResponseVO<User> login(String userCode, String userPassword) {
        User user = userMapper.findByUserCode(userCode);
        if (user == null) {
            return ResponseVO.ff(ResponseEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        if (!user.getUserPassword().equalsIgnoreCase(
                DigestUtils.md5DigestAsHex(userPassword.getBytes(StandardCharsets.UTF_8)))) {
            return ResponseVO.ff(ResponseEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        return ResponseVO.success(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseVO setInfo(User user) throws DateTimeParseException {
        User u = userMapper.findByUserCodeAll(user.getUserCode());
        if (u == null) {
            return ResponseVO.error(ResponseEnum.USER_NO_EXIT, "查无此人");
        }
        user.setUserId(u.getUserId());

        //user1 代表原班级   user代表现班级
        if (user.getUserClassCode() != null && !user.getUserClassCode().equals("")) {
            classesService.delStu(u.getUserClassCode(), user.getUserCode());
            classesService.addStu(user.getUserClassCode(), user.getUserCode());
        }
        return Util.ff(userMapper.updateById(user));
    }

    @Override
    public ResponseVO deleteUser(String value1, String userCode) {
        if (!value1.equals(Constant.PASS_DEL))
            return ResponseVO.error(ResponseEnum.PASSWORD_ERROR,"指令错误");
        User user = userMapper.findByUserCode(userCode);
        if (user == null) {
            return ResponseVO.error(ResponseEnum.USER_NO_EXIT, "学号有误");
        }
        if (user.getUserRole() == 0) {
            classesService.delStu(user.getUserClassCode(), userCode);
        }
        user.setUserDeleted(1);   // 逻辑删除
        return Util.ff(userMapper.updateById(user));
    }

    @Override
    public ResponseVO password(String userCode, String oldPsw, String newPsw) {
        User user = userMapper.findByUserCode(userCode);
        if (!user.getUserPassword().equalsIgnoreCase(
                DigestUtils.md5DigestAsHex(oldPsw.getBytes(StandardCharsets.UTF_8)))) {
            log.info("旧密码错误");
            return ResponseVO.ff(ResponseEnum.OLD_PASSWORD_ERROR);
        }
        if (userCode.equals("admin") || userCode.equals("teacher") || userCode.equals("student"))
            return ResponseVO.ff(ResponseEnum.SUCCESS, "逻辑修改密码成功。但这是公用测试号，密码不变");
        user.setUserPassword(DigestUtils.md5DigestAsHex(newPsw.getBytes(StandardCharsets.UTF_8)));
        return Util.ff(userMapper.updateById(user));
    }

    @Override
    public ResponseVO setPsw(String value1, String userCode, String newPsw) {   //管理员权限的
        if (!value1.equals(Constant.PASS_RESET))
            return ResponseVO.error(ResponseEnum.PASSWORD_ERROR,"指令错误");
        User user = userMapper.findByUserCodeAll(userCode);
        if (user == null)
            return ResponseVO.ff(ResponseEnum.USER_NO_EXIT);
        if (user.getUserDeleted() == 1)
            return ResponseVO.error(ResponseEnum.USER_NO_EXIT, "用户被删了");
        if (userCode.equals("admin") || userCode.equals("teacher") || userCode.equals("student"))
            return ResponseVO.error(ResponseEnum.SUCCESS, "逻辑修改密码成功。但这是公用测试号，密码不变");
        user.setUserPassword(DigestUtils.md5DigestAsHex(newPsw.getBytes(StandardCharsets.UTF_8)));
        return Util.ff(userMapper.updateById(user));
    }

    @Override
    public User findByUserCode(String userCode) {
        User user = userMapper.findByUserCode(userCode);
        if (user == null)
            throw new RuntimeException("账号不存在...");
        return user;
    }

    @Override
    public User findByUserCodeAll(String userCode) {
        User user = userMapper.findByUserCodeAll(userCode);
        if (user == null)
            throw new RuntimeException("账号不存在...");
        return user;
    }

    @Override
    public User findByOpenid(String openid) {
        return userMapper.findByOpenId(openid);
    }

    @Override
    public ResponseVO<User> bang(String openid, String uerCode) {
        log.info("{}  {}",openid, uerCode);
        User user = userMapper.findByOpenId(openid);
        if (user != null){
            if (!user.getUserCode().equals(uerCode)){
                throw new RuntimeException("该微信已绑定其他账号");
            }
            return ResponseVO.success(user);
        }
        user = userMapper.findByUserCode(uerCode);
        if (user == null)
            throw new RuntimeException("您要绑定的账号有误");
        user.setUserOpenid(openid);
        Util.ff(userMapper.updateById(user));
        pushMsgService.pushMsg(openid, user.getUserName());
        return ResponseVO.success(user);
    }

    @Override
    public ResponseVO setAvatar(String userCode, String imgUrl) {
        User user = userMapper.findByUserCode(userCode);
        if (user == null) {
            return ResponseVO.error(ResponseEnum.USER_NO_EXIT, "学号有误");
        }
        user.setUserAvatar(imgUrl);
        return Util.ff(userMapper.updateById(user));
    }

    @Override
    public ResponseVO getUsers(String user, Integer role, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(role != -1)
            queryWrapper.in("user_role", role);
        if(StringUtils.isNotEmpty(user))
            queryWrapper.and(wrapper-> wrapper.eq("user_code",user).or().like("user_name", user));
        List<User> users = userMapper.selectList(queryWrapper);
        return ResponseVO.success(Util.pageInfo(users));
    }
}

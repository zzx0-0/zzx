package com.example.mnnu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mnnu.config.Constant;
import com.example.mnnu.dao.ClassesMapper;
import com.example.mnnu.dao.UserMapper;
import com.example.mnnu.enums.ResponseEnum;
import com.example.mnnu.pojo.Classes;
import com.example.mnnu.pojo.User;
import com.example.mnnu.service.IClassesService;
import com.example.mnnu.utils.Util;
import com.example.mnnu.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ClassesServiceImpl implements IClassesService {

    @Autowired
    private ClassesMapper classesMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public int addStu(String classCode, String stuCode) {
        Classes classes = classesMapper.findByClassCode(classCode);
        if (classes == null) {
            classes = new Classes(classCode, stuCode);
            log.info("新增班级{}， 学生{}", classCode, stuCode);
            return classesMapper.insert(classes);
        } else {
            classes.setClassStuCount(classes.getClassStuCount() + 1);
            return classesMapper.updateById(classes);
        }
    }

    @Override
    public int delStu(String classCode, String stuCode) {
        Classes classes = classesMapper.findByClassCode(classCode);

        // 删除
        if (classes.getClassStuCount() == 1) {     //只有1名学生的，直接整个班级删掉
            log.info("删除班级{}， 学生{}", classCode, stuCode);
            return classesMapper.deleteById(classes.getClassId());
        }
        classes.setClassStuCount(classes.getClassStuCount() - 1);
        return classesMapper.updateById(classes);
    }

    @Override
    public ResponseVO setClassName(String classCode, String className) {
        Classes classes = classesMapper.findByClassCode(classCode);
        if (classes == null) {
            return ResponseVO.error(ResponseEnum.ERROR, "该班级代码有误");
        }
        classes.setClassName(className);
        return Util.ff(classesMapper.updateById(classes));
    }

    @Override
    public ResponseVO<IPage> showClass(String clas, Integer pageNum, Integer pageSize) {
        Page<Classes> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Classes> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(clas)) {
            queryWrapper.like("class_code", clas).or().like("class_name",clas);
        }
        Page<Classes> classesPage = classesMapper.selectPage(page, queryWrapper);
        return ResponseVO.success(classesPage);
    }

    @Override
    public ResponseVO getClassPension(String student, String classCode) {
        Classes classes = classesMapper.findByClassCode(classCode);
        if (classes == null){
            throw new RuntimeException("查无此班级");
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_class_code", classCode);
        queryWrapper.in("user_deleted", 0);
        if (StringUtils.isNotEmpty(student)) {
            queryWrapper.and(wrapper -> wrapper.eq("user_code",student).or().like("user_name",student));
        }
        queryWrapper.orderByAsc("user_code");
        List<User> userList = userMapper.selectList(queryWrapper);
        if (StringUtils.isEmpty(student)) {
            if (classes.getClassStuCount() != userList.size()) {
                // 发送消息
                throw new RuntimeException("班级学生人数对不上");
            }
        }
        return ResponseVO.success(Util.pageInfo(userList));
    }

}

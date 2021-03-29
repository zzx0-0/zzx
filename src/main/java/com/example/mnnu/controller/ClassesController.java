package com.example.mnnu.controller;

import com.example.mnnu.service.IClassesService;
import com.example.mnnu.utils.Util;
import com.example.mnnu.vo.ResponseVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Slf4j
@RestController
@Api(value = "班级管理类")
public class ClassesController {

    @Autowired
    private IClassesService classesService;

    @GetMapping("/1/classes")
    @ApiOperation(value = "显示所有班级")
    public ResponseVO<PageInfo> showC(@RequestParam(required = false) String clas,
                                      @RequestParam(defaultValue = "1") Integer pageNum,
                                      @RequestParam(defaultValue = "10") Integer pageSize) {
        return classesService.showClass(clas, pageNum, pageSize);
    }

    @GetMapping("/1/classes/{classCode}")
    @ApiOperation(value = "获取班级人员名单")
    public ResponseVO getClassPension(@PathVariable String classCode,
                                      @RequestParam(required = false) String student) {
        return classesService.getClassPension(student,classCode);
    }

    @PutMapping("/1/className")
    @ApiOperation(value = "设置班级名")
    public ResponseVO setClassName(@RequestParam String classCode,
                                   @RequestParam String className) {
        return classesService.setClassName(classCode, className);
    }

}

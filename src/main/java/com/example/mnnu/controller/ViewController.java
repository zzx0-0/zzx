package com.example.mnnu.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@Api(value = "ModelAndView Controller", description = "无用")
public class ViewController {

    @GetMapping("/0")
    public String student() {
        return "student/student";
    }

    @GetMapping("/1")
    public String teacher( ) {
        return "teacher/teacher";
    }

    @GetMapping("/2")
    public String admin() {
        return "admin/admin";
    }

    @GetMapping("/i/set")
    public String set() {
        return "i/set";
    }

    @GetMapping("/i/photo")
    public String photo() {
        return "i/photo";
    }

    @GetMapping("/2/file")
    public String file() {
        return "admin/file";
    }

    @GetMapping("/2/u")
    public String logon() {
        return "admin/userform";
    }

    @GetMapping("/i/password")
    public String password() {
        return "i/password";
    }

    @GetMapping("/0/sub")
    public String sub() {
        return "student/sub";
    }

    @GetMapping({"/1/exam", "/0/exam"})
    public String exam() {
        return "teacher/exam";
    }

    @GetMapping({"/1/showExam"})
    public String exam1() {
        return "teacher/showExam1";
    }

    @GetMapping("/0/showExam")
    public String exam0() {
        return "student/showExam0";
    }

    @GetMapping( {"/2/pro", "/1/pro", "/0/pro"})
    public String pro() {
        return "i/problem";
    }

    @GetMapping("/1/showPro")
    public String pro1() {
        return "teacher/showPro1";
    }

    @GetMapping("/2/showPro")
    public String pro2() {
        return "admin/showPro2";
    }

    @GetMapping("/2/allUsers")
    public String user() {
        return "admin/users";
    }

    @GetMapping("/1/showClass")
    public String showClass(){
        return "teacher/showClass";
    }

    @GetMapping("/i/tool")
    public String tool() {
        return "i/tool";
    }

    @GetMapping("/1/bar")
    public String bar() {
        return "teacher/bar";
    }

    @GetMapping({"/i/line"})
    public String line() {
        return "student/line";
    }

    @GetMapping("/1/classP")
    public String classP() {
        return "teacher/showClassStaff";
    }

    @GetMapping("/1/judgeOne")
    public String judgeOne() {
        return "teacher/judgeOne";
    }

    @GetMapping("/1/jO")
    public String jO() {
        return "teacher/jO";
    }

}

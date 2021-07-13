package com.example.mnnu.controller;

import com.example.mnnu.form.ExamSaveForm;
import com.example.mnnu.pojo.Exam;
import com.example.mnnu.service.IExamService;
import com.example.mnnu.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@Api(value = "Exam管理类")
public class ExamController {

    @Autowired
    private IExamService examService;

    @PostMapping("/1/exam")
    @ApiOperation(value = "添加/更新 试卷，自动生成试卷题目列表")
    public ResponseVO save(@Valid @RequestBody ExamSaveForm examSaveForm) {
        Exam exam = examService.changeToExam(examSaveForm);
        if (exam.getExamId() == null) {
            return examService.add(exam);
        }
        return examService.upd(exam);
    }

    @ApiOperation(value = "删除 试卷")
    @DeleteMapping("/1/exam/{examId}")
    public ResponseVO delete(@PathVariable Long examId) {
        return examService.delete(examId);
    }

    @GetMapping({"/1/examShow", "/0/examShow"})
    public ResponseVO exams(@RequestParam(defaultValue = "1") Integer pageNum,
                            @RequestParam(defaultValue = "10") Integer pageSize) {
        return examService.show(pageNum, pageSize);
    }

    @ApiOperation(value = "具体展示某试卷")
    @GetMapping("/1/examDetail")
    public ResponseVO detail(@RequestParam Long examId) {
        return examService.detail(examId);
    }


    @ApiOperation(value = "进入考试")
    @GetMapping("/0/enter")
    public ResponseVO enter(@RequestParam(name = "examId") Long examId,
                                      @RequestParam(name = "examPassword") String examPassword,
                                      @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                      @RequestParam(required = false, defaultValue = "1") Integer pageSize) {
        return examService.enter(examId, examPassword, pageNum, pageSize);
    }

}

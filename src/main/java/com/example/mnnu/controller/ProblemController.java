package com.example.mnnu.controller;

import com.example.mnnu.pojo.Problem;
import com.example.mnnu.service.IProblemService;
import com.example.mnnu.utils.Util;
import com.example.mnnu.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Api(value = "Problem管理类")
public class ProblemController {

    @Autowired
    private IProblemService problemService;

    @PostMapping("/2/pro")
    @ApiOperation(value = "添加/更新 题目")
    public ResponseVO save(@Valid @RequestBody Problem problem) {
        if (problem.getProblemId() == null)
            return problemService.addPro(problem);
        return problemService.updatePro(problem);
    }

    @DeleteMapping({"/2/pro/{proId}"})
    @ApiOperation(value = "删除 题目")
    public ResponseVO delete(@PathVariable("proId") Long problemId) {
        return problemService.delete(problemId);
    }

    @GetMapping({"/2/proShow", "/1/proShow"})
    public ResponseVO showAll(@RequestParam(name = "type", defaultValue = "-1") Integer problemType,
                             @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                             @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return problemService.showPro(problemType, pageNum, pageSize);
    }


    @GetMapping("/i/pro")
    @ApiOperation(value = "具体展示某题目")
    public ResponseVO showOne(@RequestParam(name = "proId") Long problemId) {
        return problemService.showOne(problemId);
    }

}

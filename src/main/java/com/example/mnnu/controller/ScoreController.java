package com.example.mnnu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.mnnu.dto.Judge;
import com.example.mnnu.service.IScoreService;
import com.example.mnnu.util.Util;
import com.example.mnnu.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigDecimal;

@Slf4j
@RestController
@Api(value = "关于成绩的")
public class ScoreController {

    @Autowired
    private IScoreService scoreService;

    @ApiOperation(value = "提交试卷，放到MQ队列，过一段时间重刷，便能得知答案及本试卷信息")
    @PostMapping("/0/sub")
    public ResponseVO sub(@Valid @RequestBody Judge judge, HttpSession session) {
        String userCode = (String) session.getAttribute("currentUserCode");
        if (judge.getUserCode() != null && !judge.getUserCode().equals(userCode)){
            throw new RuntimeException("提交的学号与用户本身信息有误");
        }
        return scoreService.sub(judge);
    }

    @ApiOperation(value = "给出用户所有的考试成绩")
    @GetMapping({"/i/score/{userCode}"})
    public ResponseVO userAllScore(@PathVariable(required = false) String userCode, HttpSession session) {
        int role = Util.getCurrentUserRole(session);
        if (role == 0) {
            userCode = Util.getCurrentUserCode(session);
        }
        return scoreService.getUserAllScore(userCode);
    }

    @ApiOperation(value = "某次考试某人的成绩")
    @GetMapping({"/0/score", "/1/score"})
    public ResponseVO score(@RequestParam Long examId,
                            @RequestParam(required = false) String userCode,
                            HttpSession session) {
        if (Util.getCurrentUserRole(session) == 0) {
            userCode = Util.getCurrentUserCode(session);
        }
        return scoreService.score(examId, userCode);
    }

    @ApiOperation(value = "给出某次考试所有人的成绩")
    @GetMapping({"/1/examScore/{examId}"})
    public ResponseVO examAllScore(@PathVariable Long examId) {
        return scoreService.getExamAllScore(examId);
    }



    @GetMapping("/1/judgeO")
    @ApiOperation(value = "显示所有需要教师批改的")
    public ResponseVO<IPage> judgeO(@RequestParam(defaultValue = "[0, 1]") String judgeStatus,
                                    @RequestParam(required = false) String problemId,
                                    @RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer pageSize) {
       // List status = Util.StringChangeToList(judgeStatus, Integer.class);
        return scoreService.getJudgeOnes( problemId, pageNum, pageSize);
    }

    @GetMapping("/1/getJO/{jId}")
    @ApiOperation(value = "具体显示某改题")
    public ResponseVO getJO(@PathVariable Integer jId) {
        return scoreService.getJO(jId);
    }

    @PutMapping("/1/updS")
    @ApiOperation(value = "更新成绩（某次手工判题的成绩改为score）")
    public ResponseVO updateScore(@RequestParam Long jId, @RequestParam BigDecimal score,
                                  HttpSession session) {
        String userCode = Util.getCurrentUserCode(session);
        return scoreService.updateScore(jId, score, userCode);
    }

    @GetMapping("/1/que")
    @ApiOperation(value = "学生质疑某题成绩")
    public ResponseVO que(@RequestParam String userCode, @RequestParam Long examId, @RequestParam Long proId,
                          @RequestParam BigDecimal score /* 此score指原分数 */) {
        return scoreService.que(userCode, examId, proId, score);
    }

}

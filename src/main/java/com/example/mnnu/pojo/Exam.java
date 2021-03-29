package com.example.mnnu.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import java.time.LocalDateTime;

@Data
@ToString
@TableName("exam")
public class Exam {

    @TableId(type = IdType.AUTO)
    private Long examId;

    private String examName;

    private BigDecimal examLength;    //考试时长，小时为单位

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")           //是用来接收前端传入的字符串时间，解析成Date
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GTM+8")    //是用来向前端传送Date时进行解析成字符串
    private LocalDateTime examBeginTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GTM+8")
    private LocalDateTime examEndTime;

   // @JsonIgnore
    private String examPassword;

    private String examProblemId;

    @NotBlank
    private String examPro;   // 新增

    private Integer examProCount;   // 新增

//    private String examProblemScore;

//    private String examProblemType;

//    private String examStudentCode;

//    private String examCreateBy;

//    private Integer examEnterCount;

//    private BigDecimal examAvgScore;

//    private BigDecimal examMaxScore;

//    private BigDecimal examMinScore;

    private String examStuRank;    //考试排名情况

    private BigDecimal examAvgLikely;

    @JsonIgnore
    private LocalDateTime createTime;

    @JsonIgnore
    private LocalDateTime updateTime;

    public Exam() {}

}

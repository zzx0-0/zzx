package com.example.mnnu.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ToString
@TableName("judge_one")
public class JudgeOne {

    @TableId(type = IdType.AUTO)
    private Long judgeId;

    // 谁提交的，哪一次考试中提交的， 是为了教师人工判题...
    private String userCode;
    private Long examId;

    @NotNull
    private Long problemId;

    @NotBlank
    private String sub;


    @TableField(exist = false)
    private String problemText;
    @TableField(exist = false)
    private String problemAnswer;

    private BigDecimal score;   // 该小题分值

    private BigDecimal give;    // 判分

    private Integer judged;     // 是否已改过： 0-还未判断；1-判成

    private String judgeBy;

    @JsonIgnore
    private LocalDateTime createTime;

    @JsonIgnore
    private LocalDateTime updateTime;

    public JudgeOne() {}

}

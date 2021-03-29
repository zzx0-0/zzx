package com.example.mnnu.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ToString
@TableName("score")
public class Score {

    @TableId(type = IdType.AUTO)
    private Long scoreId;

    @NotBlank
    private String userCode;

    @NotBlank
    private Long examId;

    private String submitAnswer;

    @NotBlank
    private BigDecimal score;

    private String remark;

    @JsonIgnore
    private LocalDateTime createTime;

    @JsonIgnore
    private LocalDateTime updateTime;

    public Score() {
    }

    public Score(String userCode, Long examId, String submitAnswer) {
        this.userCode = userCode;
        this.examId = examId;
        this.submitAnswer = submitAnswer;
    }

}

package com.example.mnnu.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ProblemVO {

    private String problemText;

    private String ProblemAnswer;

    @JsonIgnore
    private Integer problemType;

}

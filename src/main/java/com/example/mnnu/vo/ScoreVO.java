package com.example.mnnu.vo;

import com.example.mnnu.pojo.Score;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ScoreVO {

    private Integer count;

    private String message;

 //   private BigDecimal averageScore;

 //   private BigDecimal maxScore;

 //   private BigDecimal minScore;

    private List<Score> scoreList;

    private List<BigDecimal> scList;   // 这是给折线图用的

    @JsonProperty("eList")
    private List<Long> eList;   // 这是给折线图用的

    private List<Integer> numList;    // 这是给柱形图用的

}

package com.example.mnnu.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ExamVO<T> implements Serializable {

    private static final long serialVersionUID = -6055816962741956948L;

    private BigDecimal score;

    private List<ProblemVO> problemVOList;

    private List subList;

}

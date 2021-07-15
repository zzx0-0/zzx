package com.example.mnnu.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Judge {

    @NotBlank
    private String userCode;

    @NotBlank
    private String examId;

    private String subList;


    public Judge() {
    }

    public Long getExamId(){return Long.valueOf(examId);}

//    public List getSubList() {
//        return Util.StringChangeToList(subList, String.class);
//    }

}

package com.example.mnnu.dto;

import com.example.mnnu.utils.Util;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

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

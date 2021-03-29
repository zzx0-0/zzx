package com.example.mnnu.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ExamSaveForm {

    private Long examId;

    @NotBlank
    private String examName;

    @NotBlank
    private String examBeginTime;

    @NotBlank
    private String examEndTime;

    private String examPassword;

    private String examProblemId;

    @NotBlank
    private String examPro;   // 新增

//    private String examProblemScore;

//   private String examProblemType;

//    private String examStudentCode;

    public ExamSaveForm() {
    }

}

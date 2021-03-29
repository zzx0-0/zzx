package com.example.mnnu.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserLoginForm {

    @NotBlank(message = "用户名不能为空")
    private String userCode;

    @NotBlank(message = "密码不能为空")
    private String userPassword;

    private String remember = "off";   //默认

}

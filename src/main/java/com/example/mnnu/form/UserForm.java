package com.example.mnnu.form;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserForm {

    private Long userId;

    @NotBlank
    private String userCode;

    @NotBlank
    private String userName;

   // @NotBlank
    private String userPassword;

    @Range(min = 0, max = 2)
    private Integer userRole;    //默认0学生

    @Pattern(regexp = "^$|[男女]", message = "性别为男或女(但可以为空)")     // 可为空
    private String userGender;

    @Pattern(regexp = "^$|[1-9][0-9]{10}", message = "手机号格式有误")     // 可为空
    private String userPhone;

    private String captcha;

    private String userDeleted;   // 设置可重新启用

    public UserForm() { }

}

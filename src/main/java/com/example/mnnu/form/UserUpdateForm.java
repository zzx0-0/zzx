package com.example.mnnu.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserUpdateForm {

    @NotBlank
    private String userCode;

    private String userName;

    @Pattern(regexp = "^$|[男女]", message = "性别为男或女")
    private String userGender;

    private String userPetName;

    private String userMotto;

    @Pattern(regexp = "^$|[1-9][0-9]{10}", message = "手机号格式有误")
    private String userPhone;

    private String userMail;

    private String userBirthday;

    private String userAvatar;

   // private String userFaceFeature;      //人脸特征

   // private String userFingerprintFeature;    //指纹特征

}

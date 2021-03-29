package com.example.mnnu.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/*
    @NotNull：不能为null，但可以为empty(""," ","   ")    //用于 任意类型
    @NotEmpty：不能为null，而且长度必须大于0 (" ","  ")   //@NotEmpty 只能用于对String 、 Collection 或 array 字段的注解， 其他的 就不行
    @NotBlank：只能作用在String上，不能为null，而且调用trim()后，长度必须大于0   即：必须有实际字符
 */
@Data
@ToString
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long userId;

    private String userCode;

    private String userName;

    @JsonIgnore
    private String userPassword;

    private Integer userRole;    //可不传，默认0学生

    private String userClassCode;

    private String userOpenid;

    //@TableLogic
    //@TableField(select = false)    // 排除删除标识字段.
    private Integer userDeleted;   // 0-还在； 1-被删了

    private String userGender;     //性别

    private String userPetName;

    private String userMotto;

    private String userPhone;

    private String userMail;

    //@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GTM+8")
    private LocalDate userBirthday;

    private String userAvatar;     //头像图片地址

    @JsonIgnore
    private String userFaceFeature;      //人脸特征

    @JsonIgnore
    private String userFingerprintFeature;    //指纹特征


    private BigDecimal userPerFacePasstime;    //人脸识别平均时长

    private BigDecimal userPerFingerprintPasstime;   //指纹识别平均时长

    private BigDecimal userPerSubmitExamTime;       //学生交卷平均时长

    private BigDecimal userPerExamScore;      //学生答卷平均分

    private BigDecimal userMaxExamScore;

    private BigDecimal userMinExamScore;

    private Integer userExamed;     //学生参加考试次数/教师举办考试次数

    private Integer userFeedbackCount;    //反馈次数

    @JsonIgnore
    private LocalDateTime createTime;

    @JsonIgnore
    private LocalDateTime updateTime;

    public User() {
    }

    public String getUserPetName() {
        if (userPetName == null || userPetName.equals("")) {
            return userName;
        }
        return userPetName;
    }

}

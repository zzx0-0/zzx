package com.example.mnnu.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@ToString
@TableName("classes")
public class Classes {

    @TableId(type = IdType.AUTO)
    private Long classId;

    @NotBlank
    @Length(min = 8, max = 8, message = "班级代码为8位，即该班学生学号前8位")
    private String classCode;

    private String className;

    //private String classStuCode;

    private Integer classStuCount;

    @JsonIgnore
    private String classTeacherCode;

    private Integer classTotalExamCount;

    @JsonIgnore
    private LocalDateTime createTime;

    @JsonIgnore
    private LocalDateTime updateTime;

    public Classes() { }

    public Classes(String classCode, String stuCode) {    // 建空班级
        this.classCode = classCode;
        Set<String> stuCodes = new HashSet<>();
        stuCodes.add(stuCode);
      //  this.classStuCode = stuCodes.toString();
        this.classStuCount = 1;
    }

}

package com.example.mnnu.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {

    STUDENT(0, "学生"),

    TEACHER(1, "教师"),

    ADMIN(2, "管理员"),

    ;


    Integer code;

    String desc;

    RoleEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
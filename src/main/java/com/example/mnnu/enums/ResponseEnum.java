package com.example.mnnu.enums;

import lombok.Getter;

@Getter
public enum ResponseEnum {

    SUCCESS(0, "成功"),

    LOGOUT_SUCCESS(10, "退出登录成功"),

    SUBMIT_SUCCESS(20, "试卷已提交，正在判断中，请稍后刷新页面读取成绩....."),


    ERROR(-1, "服务器错误"),

    USERNAME_OR_PASSWORD_ERROR(1, "用户名或密码错误"),

    OLD_PASSWORD_ERROR(1, "原密码错误"),

    PASSWORD_ERROR(2, "密码错误"),

    USER_EXIT(3, "用户已存在"),

    USER_NO_EXIT(4, "用户不存在"),

    USER_ONCE_EXIT(5, "用户曾经存在"),

    PARAM_ERROR(6, "参数错误"),

    ERROR_TIME(7,"非考试时间"),

    MYSQL_ERROR(8,"数据库操作失败"),

    REDIS_ERROR(9,"REDIS没打开"),



    EXAM_ERROR(11, "试卷总分不为100"),

    NOT_ACCEPT(22, "无此权限"),

    OTHERS_ERROR(33, "其他异常"),

    ;


    Integer code;

    String desc;

    ResponseEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}

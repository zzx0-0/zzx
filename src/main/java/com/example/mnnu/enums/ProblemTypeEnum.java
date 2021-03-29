package com.example.mnnu.enums;

import lombok.Getter;

@Getter
public enum ProblemTypeEnum {

    CHOICE(1, "选择题"),

    COMPLETION(2, "填空题"),

    JUDGE(3, "判断题"),

    COMPREHENSIVE(4, "综合题"),

    WRITE(5, "书写"),

    ;

    Integer code;

    String desc;

    ProblemTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    //  通过枚举字段code得到枚举
    public static ProblemTypeEnum getEnum(Integer code) {
        for (ProblemTypeEnum typeEnum : ProblemTypeEnum.class.getEnumConstants()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum;
            }
        }
        throw new RuntimeException("错误，枚举里无该字段");
    }

}

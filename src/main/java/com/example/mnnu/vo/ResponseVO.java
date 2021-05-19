package com.example.mnnu.vo;

import com.example.mnnu.enums.ResponseEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)     //JSON为null不返回
public class ResponseVO<T> implements Serializable {

    private static final long serialVersionUID = -5892025448367232104L;

    private Integer code;

    //@JsonProperty("massage")    //返回前端的字段
    private String msg;

    private T data;

    public ResponseVO() { }

    public ResponseVO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseVO(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ResponseVO<T> success() {
        return new ResponseVO<>(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getDesc());
    }

    public static <T> ResponseVO<T> success(T data) {
        return new ResponseVO<>(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getDesc(), data);
    }

    public static <T> ResponseVO<T> error(ResponseEnum responseEnum, String msg) {
        return new ResponseVO<>(responseEnum.getCode(), msg);
    }

    public static <T> ResponseVO<T> ff(ResponseEnum responseEnum, String msg) {
        return new ResponseVO<>(responseEnum.getCode(), msg);
    }

    public static <T> ResponseVO<T> ff(ResponseEnum responseEnum) {
        return new ResponseVO<>(responseEnum.getCode(), responseEnum.getDesc());
    }

}
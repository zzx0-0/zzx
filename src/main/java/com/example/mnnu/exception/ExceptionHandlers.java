package com.example.mnnu.exception;

import com.example.mnnu.config.Constant;
import com.example.mnnu.controller.MsgController;
import com.example.mnnu.enums.ResponseEnum;
import com.example.mnnu.vo.ResponseVO;
import io.sentry.Sentry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import java.net.BindException;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@ControllerAdvice
public class ExceptionHandlers {

    @Autowired
    private MsgController msgController;

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
//	@ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseVO handle(RuntimeException e) {
        e.printStackTrace();
        Sentry.captureException(e);
        return ResponseVO.error(ResponseEnum.ERROR, e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public ResponseVO MethodArgumentNotValidExceptionHandle(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        Objects.requireNonNull(bindingResult.getFieldError());
        return ResponseVO.error(ResponseEnum.PARAM_ERROR, bindingResult.getFieldError().getDefaultMessage());
    }

    @ExceptionHandler({BindException.class})
    @ResponseBody
    public ResponseVO BindExceptionHandle(BindException e) {
        return ResponseVO.error(ResponseEnum.PARAM_ERROR, "参数错误");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ResponseVO requestMissingServletRequest(MissingServletRequestParameterException e) {
        return ResponseVO.error(ResponseEnum.PARAM_ERROR, "缺少参数: " + e.getParameterName());
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseBody
    public ResponseVO DateTimeParseExceptionHandler(Exception e) {
        e.printStackTrace();
        return ResponseVO.error(ResponseEnum.OTHERS_ERROR, "时间格式有问题");
    }

    @ExceptionHandler(MessagingException.class)
    @ResponseBody
    public ResponseVO MessagingExceptionHandler(Exception e) {
        e.printStackTrace();
        return ResponseVO.error(ResponseEnum.OTHERS_ERROR, "邮件发送失败");
    }

    @ExceptionHandler(RedisConnectionFailureException.class)
    @ResponseBody
    public ResponseVO RedisConnectionFailureExceptionHandler(Exception e) {
        e.printStackTrace();
        msgController.getMail(Constant.eMail, "警告", "服务器redis出问题");
        return ResponseVO.error(ResponseEnum.REDIS_ERROR, "Sorry, there is something wrong");
    }

    // 放在最后，捕获未处理的其他异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseVO ExceptionHandler(Exception e) {
        e.printStackTrace();
        Sentry.captureException(e);
        return ResponseVO.error(ResponseEnum.OTHERS_ERROR, e.getMessage());
    }

}

package com.example.mnnu.controller;

import com.example.mnnu.service.IMailService;
import com.example.mnnu.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Slf4j
@Api(value = "发送消息类")
@RestController
public class MsgController {

    @Autowired
    private IMailService mailService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/getOTP")
    @ApiOperation(value = "发送短信")
    public ResponseVO getOTP(@RequestParam("phone") String phone) {
        int random = (int) (Math.random()*899999 +100000);
        try {
            redisTemplate.opsForValue().set(phone, String.valueOf(random), 5, TimeUnit.MINUTES);  //有效时间5分钟
        } catch (RedisConnectionFailureException e) {
            throw new RuntimeException("请联系管理员： Redis连接有问题");
        }

        mailService.sendMsg(phone, String.valueOf(random));
        log.info("手机号{}, 验证码={}", phone,random);
        return ResponseVO.success();
    }

    @PostMapping("/getMail")
    @ApiOperation(value = "发送EMail")
    public ResponseVO getMail(@RequestParam("to") String to,
                              @RequestParam("subject") String subject,
                              @RequestParam("content") String content) {
        mailService.sendSimpleMail(to, subject, content);
        return ResponseVO.success();
    }

}

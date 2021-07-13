package com.example.mnnu.service;

import com.alibaba.fastjson.JSON;
import com.example.mnnu.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Slf4j
@Service
public class PushMsgService {

    @Autowired
    private RestTemplate restTemplate;

    public void pushMsg(String openid, String name) throws UnsupportedEncodingException {
        String head = "绑定成功通知";
        String body = "您已成功绑定用户：" + name;

        String url = "http://47.107.92.114:8080/send?openid=" + URLEncoder.encode(openid,"UTF-8") +
                "&head=" + URLEncoder.encode(head,"UTF-8") +
                "&body=" + URLEncoder.encode(body,"UTF-8");
        String response = restTemplate.getForObject(url, String.class);
        log.info("发送微信消息返回={}",response);
        ResponseVO VO = JSON.parseObject(response,ResponseVO.class);


    }


}

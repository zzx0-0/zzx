package com.example.mnnu.service;

import com.example.mnnu.config.YmlConfig;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class PushMsgService {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private YmlConfig ymlConfig;

    private WxMpTemplateMessage getTemplateMessage(){
        WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
        templateMessage.setTemplateId(ymlConfig.getWX_TemplateId());
        return templateMessage;
    }

    public void pushMsg(String openid, String name) {
        WxMpTemplateMessage templateMessage = getTemplateMessage();
        templateMessage.setToUser(openid);

        List<WxMpTemplateData> data = Arrays.asList(
                new WxMpTemplateData("head", "绑定成功通知", "red"),
                new WxMpTemplateData("body", "您已成功绑定用户： " + name )
        );
        templateMessage.setData(data);
        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (WxErrorException e) {
            log.error("【微信模版消息】发送失败", e);
        }
    }

    public void pushMsg(String openid, String head, String body) {
        WxMpTemplateMessage templateMessage = getTemplateMessage();
        templateMessage.setToUser(openid);

        List<WxMpTemplateData> data = Arrays.asList(
                new WxMpTemplateData("head", head, "red"),
                new WxMpTemplateData("body", body)
        );
        templateMessage.setData(data);
        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (WxErrorException e) {
            log.error("【微信模版消息】发送失败", e);
        }
    }

}

package com.example.mnnu.service;

import com.alibaba.fastjson.JSON;
import com.example.mnnu.config.Constant;
import com.example.mnnu.controller.MsgController;
import com.example.mnnu.dto.Judge;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RabbitListener(queues = Constant.EXAM_JUDGE)
@Component
@Slf4j
public class MQListener {

    @Autowired
    private IScoreService scoreService;

    @Autowired
    private MsgController msgController;

    // @RabbitListener(queues = Constant.EXAM_JUDGE)
    @RabbitHandler
    public void process(String msg, Channel channel, Message message) throws IOException {
        log.info("【收到MQ消息】=> msg={}", msg);

        Judge judge = JSON.parseObject(msg, Judge.class);
        try {
            scoreService.judge(judge);
        } catch (Exception e) {
            e.printStackTrace();
            msgController.getMail(Constant.eMail, "MQ", msg);
        } finally {
            log.info("确认消费MQ消息");
            // 手动签收[参数1:消息投递序号,参数2:批量签收]
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

}

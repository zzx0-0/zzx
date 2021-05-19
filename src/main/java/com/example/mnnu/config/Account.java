package com.example.mnnu.config;

import com.baidu.aip.face.AipFace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Component
public class Account {

    @Autowired
    private YmlConfig ymlConfig;

    @Bean
    public AipFace client() {
        return new AipFace(ymlConfig.getBD_AppId(), ymlConfig.getBD_ApiKey(), ymlConfig.getBD_SecretKey());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

     //websocket
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }

}

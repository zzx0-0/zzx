package com.example.mnnu.config;

import com.baidu.aip.face.AipFace;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Component
public class Account {

    @Autowired
    private YmlConfig ymlConfig;

    @Bean
    public WxMpService wxMpService() {
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpInMemoryConfigStorage wxMpConfigStorage = new WxMpInMemoryConfigStorage();
        wxMpConfigStorage.setAppId(ymlConfig.getWX_MpAppId());
        wxMpConfigStorage.setSecret(ymlConfig.getWX_MpAppSecret());
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage);
        return wxMpService;
    }

    @Bean
    public AipFace client() {
        return new AipFace(ymlConfig.getBD_AppId(), ymlConfig.getBD_ApiKey(), ymlConfig.getBD_SecretKey());
    }

     //websocket
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }

}

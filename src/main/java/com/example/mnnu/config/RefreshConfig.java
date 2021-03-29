package com.example.mnnu.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class RefreshConfig {

//    @Autowired
//    private YmlConfig ymlConfig;
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
//
//    @PostMapping("/refresh")
//    public Object sync() {
//        Map<String, Integer> map = new HashMap<>();
//                     //"http://localhost:8080/actuator/bus-refresh"
//        String url = String.format("http://localhost:%s/actuator/bus-refresh", ymlConfig.getCONFIGPORT());
//        restTemplate.postForObject(url, map, Object.class);
//        map.put("code", 0);
//        return map;
//    }
}

package com.example.mnnu.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
//@RefreshScope
public class YmlConfig {

    @Value("${Path.up}") private String upPath;
    @Value("${Path.down}") private String downPath;
    @Value("${CMD}") private String cmd;


    @Value("${OSS.EndPoint}")  private String OSS_EndPoint;

    @Value("${OSS.AccessKeyId}")  private String OSS_AccessKeyId;

    @Value("${OSS.AccessKeySecret}")  private String OSS_AccessKeySecret;

    @Value("${OSS.BucketName}")  private String OSS_BucketName;


    @Value("${BD.AppId}")  private String BD_AppId;

    @Value("${BD.ApiKey}")  private String BD_ApiKey;

    @Value("${BD.SecretKey}")  private String BD_SecretKey;


}

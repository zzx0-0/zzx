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



    @Value("${WX.MpAppId}")  private String WX_MpAppId;

    @Value("${WX.MpAppSecret}")  private String WX_MpAppSecret;

    @Value("${WX.TemplateId}") private String WX_TemplateId;




    @Value("${WX.OpenAppId}")  private String WX_OpenAppId;

    @Value("${WX.OpenAppSecret}")  private String WX_OpenAppSecret;

    @Value("${WX.MchId}")  private String WX_MchId;

    @Value("${WX.MchKey}")  private String WX_MchKey;

    @Value("${WX.KeyPath}")  private String WX_KeyPath;


    @Value("${ALI.AppId}")  private String ALI_AppId;

    @Value("${ALI.PublicKey}")  private String ALI_PublicKey;

    @Value("${ALI.PrivateKey}")  private String ALI_PrivateKey;



}

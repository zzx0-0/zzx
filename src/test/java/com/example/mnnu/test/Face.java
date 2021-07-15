package com.example.mnnu.test;

import com.baidu.aip.face.AipFace;
import com.example.mnnu.config.Account;
import com.example.mnnu.config.Constant;
import com.example.mnnu.util.Util;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;

//  人脸识别
@RunWith(SpringRunner.class)
@SpringBootTest
public class Face {

    @Autowired
    private Account account;

    private static final String zzx_url = "http://zzx769394358.oss-cn-shenzhen.aliyuncs.com/2020-09-05/68218dda-3884-413b-8192-9a0bf3f7eb88.jpg";
    private static final String hou = "http://zzx769394358.oss-cn-shenzhen.aliyuncs.com/2020-09-16/aef31714-c407-4af3-a42c-514a0ee79cae.jpg";
    private static final String greentea = "http://zzx769394358.oss-cn-shenzhen.aliyuncs.com/2020-09-16/c3273051-1601-48b8-b54a-0b2fb4b8a5da.jpg";
    private static final String hou2 = "C:\\Users\\ASUS\\Desktop\\hou2.jpg";


    //  https://ai.baidu.com/ai-doc/FACE/8k37c1rqz

    @Test
    public void detect() throws JSONException {
        AipFace client = account.client();
        HashMap<String, String> options = new HashMap<>();
        options.put("face_field", "age,beauty,expression,face_shape,gender,glasses,landmark,race,quality,eye_status,emotion,face_type");
        JSONObject res = client.detect(zzx_url, Constant.ImgUrl, options);
        System.out.println(res.toString(2));
    }

    @Test
    public void search() throws IOException, JSONException {
        AipFace client = account.client();
        HashMap<String, String> options = new HashMap<>();
        options.put("user_id", "");      //当需要对特定用户进行比对时，指定user_id进行比对。即人脸认证功能。
        JSONObject res = client.search(Util.getBase64("C:\\Users\\ASUS\\Desktop\\2020-9-17.jpg"), Constant.ImgPath, Constant.AI_GROUP, null);
        System.out.println(res.toString(2));
    }

    @Test
    public void addUser() throws JSONException {
        AipFace client = account.client();
        HashMap<String, String> options = new HashMap<>();
        options.put("user_info", "绿茶");
        JSONObject res = client.addUser(greentea, "URL", Constant.AI_GROUP, "greentea", options);
        System.out.println(res.toString(2));
    }

    @Test
    public void updateUser() throws JSONException {
        AipFace client = account.client();
    }


}

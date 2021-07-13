package com.example.mnnu;

import com.alibaba.fastjson.JSON;
import com.example.mnnu.dao.ClassesMapper;
import com.example.mnnu.dao.ScoreMapper;
import com.example.mnnu.dao.UserMapper;
import com.example.mnnu.pojo.ClassA;
import com.example.mnnu.pojo.Score;
import com.example.mnnu.pojo.User;
import com.example.mnnu.pojo2.Class2;
import io.sentry.Sentry;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MnnuApplicationTests {

    @Test
    public void test() {
        try {
            throw new Exception("a test.");
        } catch (Exception e) {
            Sentry.captureException(e);
        }
    }

    @Test
    public void t() {
        String str1="i";
        String str2="i";
        System.out.println(str1 == str2);

        String s1 = new String("i");
        String s2 = new String("i");
        System.out.println(s1==s2);
    }

    @Autowired
    private ScoreMapper scoreMapper;


    @Test
    public void testAssert(){
        Score score = scoreMapper.getScore("z",1L);
        assert score != null;
        System.out.println("ok");
    }



    @Test
    public void testHash(){
        HashMap map = new HashMap();
        map.put(null, 0);
        map.put(null,1);
        map.put(0,null);
        map.put(1,null);
        map.put(0,null);
        System.out.println(JSON.toJSONString(map));

        Hashtable table = new Hashtable();
//        table.put(null, 0);
//        table.put(null, 1);
   //     table.put(0,null);
 //       table.put(1,null);
 //       table.put(0,null);
        System.out.println(JSON.toJSONString(table));

        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        concurrentHashMap.put(null, 0);
        concurrentHashMap.put(null, 1);
        concurrentHashMap.put(0,null);
        concurrentHashMap.put(1,null);
        concurrentHashMap.put(0,null);
        System.out.println(JSON.toJSONString(concurrentHashMap));
    }

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testzzx(){
        User user1 = userMapper.selectById(100068L);
        user1.setUserName("111");

        User user2 = userMapper.selectById(100068L);
        user2.setUserName("2222");

        int result = userMapper.updateById(user1);
        System.out.println(result);

        int result2 = userMapper.updateById(user2);
        System.out.println(result2);
    }
}

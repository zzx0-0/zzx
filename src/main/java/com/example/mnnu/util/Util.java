package com.example.mnnu.util;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.mnnu.config.Constant;
import com.example.mnnu.vo.ResponseVO;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@RestController
public class Util {

    public static ResponseVO ff(int row) {
        if (row == 0) {
            throw new RuntimeException("数据库操作不成功");
        }
        return ResponseVO.success();
    }

    public static IPage pageInfo(List list) {
     //   PageInfo pageInfo = new PageInfo(list);
    //    pageInfo.setList(list);
        return null;
    }

    // []
    @Synchronized
    public static <T>List StringChangeToList(String s, T t) {
        s = s.substring(1, s.length() - 1);

        String[] teaidary =  s.split(", ");

        if (t == Integer.class) {
            List<Integer> list = new ArrayList<>();
            for(String item : teaidary){
                list.add(Integer.valueOf(item));
            }
            return list;
        } else if (t == Long.class) {
            List<Long> list = new ArrayList<>();
            for(String item : teaidary){
                list.add(Long.valueOf(item));
            }
            return list;
        } else {
            List list = new ArrayList<>();
            Collections.addAll(list, teaidary);
            return list;
        }
    }

    @Synchronized
    //             [[1, 2], [3, 4], [5, 6], [7, 8]]
    public static Integer[][] StringChangeToIntList(String s, boolean flag) {
        try {
            String[][] arr = JSON.parseObject(s, String[][].class);
            Integer[][] ds = new Integer[arr.length][2];
            for(int i=0; i<arr.length; i++) {
                for (int j = 0; j < 2; j++) {
                    ds[i][j] = Integer.valueOf(arr[i][j]);
                    if (j == 1 && flag && ds[i][1] == 0) {
                        ds[i][0] = 0;
                    }
                }
            }
            return ds;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("'考试分数'请仔细填写");
        }
    }

    public static Integer findScore(Integer[][] integers, Integer i){
        int b = 0, k = 0;
        while (b < i && k <= 4){
            b = b + integers[k++][0];
        }
        if (b >= i)
            return integers[k--][1];
        throw new RuntimeException("findTwo失败");
    }


    //  对照片Base64编码
    public static String getBase64(String path) throws IOException {
        byte[] bytes = FileUtils.readFileToByteArray(new File(path));
        return Base64.encodeBase64String(bytes);
    }

    public static String getCurrentUserCode(HttpSession session){
        return (String) session.getAttribute(Constant.CURRENT_USER_CODE);
    }

    public static Integer getCurrentUserRole(HttpSession session){
        return (Integer) session.getAttribute(Constant.CURRENT_USER_ROLE);
    }

//    public static boolean isNull(String s) {
//        return s == null || s.trim().equals("");
//    }
//
//    public static boolean isNotNull(String s){
//        return !isNull(s);
//    }

}

package com.example.mnnu.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//  获取用户设备
@Slf4j
public class AgentUserKit {

    private static final String pattern = "^Mozilla/\\d\\.\\d\\s+\\(+.+?\\)";
    private static final String pattern2 = "\\(+.+?\\)";
    private static final Pattern r = Pattern.compile(pattern);
    private static final Pattern r2 = Pattern.compile(pattern2);

    public static String getDeviceInfo(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) {
            throw new RuntimeException("http头部[User-Agent]怎么会空呢");
        }
        return getDeviceInfo(userAgent);
    }

    private static String getDeviceInfo(String userAgent) {
        Matcher m = r.matcher(userAgent);
        String result = null;
        if (m.find()) {
            result = m.group(0);
        }

        try {
            Matcher m2 = r2.matcher(result);
            if (m2.find()) {
                result = m2.group(0);
            }
        } catch (NullPointerException e) {
            return "【异常】" + userAgent;
        }

        result = result.replace("(", "");
        result = result.replace(")", "");
        return result;
    }

}

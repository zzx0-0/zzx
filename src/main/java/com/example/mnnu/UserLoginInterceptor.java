package com.example.mnnu;

import com.example.mnnu.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 *
 * AOP  基于包、类、方法名
 * Interceptor 基于URL
 * IoC  控制反转
 *
 */

@Slf4j
@Component
public class UserLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String uri = request.getRequestURI();   //  request.getRequestURI()是相对路径，  URL为绝对路径

        String userCode = Util.getCurrentUserCode(request.getSession());
        if (userCode == null) {
            log.info("uri='{}'. Need login..", uri);
            response.sendRedirect("/");
            return false;
        }

        boolean f = false;
        if (uri.startsWith("/i") || uri.equals("/me"))
            f = true;
        else {
            Integer userRole = Util.getCurrentUserRole(request.getSession());
            if (userRole == 0) {
                if (uri.startsWith("/0"))
                    f = true;
            } else if (userRole == 1) {
                if (uri.startsWith("/1"))
                    f = true;
            } else if (userRole == 2) {
                if (uri.startsWith("/2"))
                    f = true;
            }
        }
    //    if (!f)    response.sendRedirect("/");

        log.info("userCode={}, uri='{}', method={}. pass={}", userCode, uri, request.getMethod(), f);
        return f;
    }

}

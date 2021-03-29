package com.example.mnnu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.AccessType;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired                // 若UserLoginInterceptor.java里也有Autowired，一定要用这种方法
    private UserLoginInterceptor userLoginInterceptor;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/error").setViewName("/error/error");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userLoginInterceptor)
                .addPathPatterns("/0/**", "/1/**", "/2/**", "/i/**", "/me");
            /*    .excludePathPatterns("/", "/login", "/logon", "/register", "/logout", "/getOTP", "/webSocket", "/error",
                        "/css/*", "/js/*", "/image/*", "/mp3/*", "/layui/**",
                        "/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/csrf",
                        "/wx/reply", "/loginBy*");
            */
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")    // 允许跨域访问的路径
                .allowedOrigins("*")    // 允许跨域访问的源
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")    // 允许请求方法
                .maxAge(168000)    // 预检间隔时间
                .allowedHeaders("*")  // 允许头部设置
                .allowCredentials(true);    // 是否发送cookie
    }

}

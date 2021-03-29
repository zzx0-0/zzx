package com.example.mnnu.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * AOP
 *
 * Around before
 * Before
 *
 * Around after
 * After
 * AfterReturning
 */

@Aspect
@Component
@Slf4j
public class HttpAspect {

//    //指定切入点表达式
//    @Pointcut("execution(public * com.example.mnnu.controller.*.*(..))")
//    public void pointcut() {}
//
//    // JoinPoint  连接点
//    @Before("pointcut()")
//    public void doBefore(JoinPoint joinPoint) {
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//
//        StringBuffer url = request.getRequestURL();
//
//        String name= joinPoint.getSignature().getDeclaringTypeName()+ "." +joinPoint.getSignature().getName();    //类方法
//
//        Object[] args = joinPoint.getArgs();
//
//        log.info("Before");
//    }
//
//    @After("pointcut()")
//    public void doAfter() {
//        log.info("After");
//    }
//
//    @AfterReturning(pointcut = "pointcut()", returning = "val")
//    public void doAfterReturning(JoinPoint joinPoint, Object val){
//        log.info("AfterReturning, 返回的内容= {}", val);
//    }
//
//    @AfterThrowing("pointcut()")
//    public void doAfterThrowing(){
//        log.info("出错啦");
//    }
//
//    @Around("pointcut()")
//    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
//        log.info("Around : before");
//        Object result = pjp.proceed();
//        log.info("Around : after");
//        return result;
//    }

}
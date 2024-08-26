package com.sparta.spartaeats.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ApiLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ApiLoggingAspect.class);

    @AfterReturning(pointcut = "execution(* com.sparta.spartaeats..controller..*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        // API 호출 메소드와 결과 로그 출력
        logger.info("Method [{}] executed with result: {}", joinPoint.getSignature(), result);
    }
}
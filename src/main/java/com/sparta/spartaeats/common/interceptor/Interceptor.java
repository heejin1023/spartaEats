package com.sparta.spartaeats.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class Interceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(Interceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("Request URL: {}", request.getRequestURL());
        logger.info("HTTP Method: {}", request.getMethod());
        return true; // true를 반환하면 요청이 계속 진행됩니다. false를 반환하면 요청이 중단됩니다.
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 이 메소드는 뷰 렌더링 전에 호출됩니다.
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("Response Status: {}", response.getStatus());
    }
}

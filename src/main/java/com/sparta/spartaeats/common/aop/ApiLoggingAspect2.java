package com.sparta.spartaeats.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.spartaeats.apiLog.domain.ApiLog;
import com.sparta.spartaeats.apiLog.service.ApiLogService;
import com.sparta.spartaeats.common.security.UserDetailsImpl;
import com.sparta.spartaeats.user.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Aspect
//@Component
@RequiredArgsConstructor
public class ApiLoggingAspect2 {

    private final ApiLogService apiLogService;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private HttpServletRequest request;

    @Around("@annotation(apiLogging)")
    public void logBefore(JoinPoint joinPoint, ApiLogging apiLogging) {
        ApiLog log = new ApiLog();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetailsImpl) {
            UserDetailsImpl userDetailsImpl = (UserDetailsImpl) principal;
            User user = userDetailsImpl.getUser();
            Long userIdx = user.getId();
            log.setClientId(userIdx);
        }

        log.setEndpoint(joinPoint.getSignature().toShortString());
        log.setHttpMethod(request.getMethod());
        //log.setRequestBody(getRequestPayload());
        log.setCreatedAt(LocalDateTime.now());

        ApiLogContext.setLog(log);
    }

    @AfterReturning(pointcut = "@annotation(apiLogging)", returning = "result")
    public void logAfter(JoinPoint joinPoint, ApiLogging apiLogging, Object result) {
        ApiLog log = ApiLogContext.getLog();
        if (log != null) {
            log.setResponseBody(result != null ? result.toString() : "No response");


            apiLogService.createLog(log);
            ApiLogContext.clear(); // Clear the context after logging
        }
    }

    private String getRequestPayload() {
        try {
            // Read request body
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = request.getReader();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            log.error("Error reading request payload", e);
            return "ERROR";
        }
    }

    public static class ApiLogContext {
        private static final ThreadLocal<ApiLog> apiLogThreadLocal = new ThreadLocal<>();

        public static void setLog(ApiLog log) {
            apiLogThreadLocal.set(log);
        }

        public static ApiLog getLog() {
            return apiLogThreadLocal.get();
        }

        public static void clear() {
            apiLogThreadLocal.remove();
        }
    }
}

package com.sparta.spartaeats.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.spartaeats.common.aop.domain.ApiLogVO;
import com.sparta.spartaeats.common.aop.service.ApiLogService;
import com.sparta.spartaeats.common.security.UserDetailsImpl;
import com.sparta.spartaeats.user.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ApiLoggingAspect {

    private final ApiLogService apiLogService;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private HttpServletRequest request;

    @Around("@annotation(apiLogging)")
    public Object logAround(ProceedingJoinPoint joinPoint, ApiLogging apiLogging) throws Throwable {
        ApiLogVO log = new ApiLogVO();

        ContentCachingRequestWrapper cachedRequest = new ContentCachingRequestWrapper(request);

        // 메서드 실행 전 로깅
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
        //ßlog.setRequestBody(getRequestPayload()); // Request body 읽기
        log.setCreatedAt(LocalDateTime.now());

        ApiLogContext.setLog(log);

        Object result;
        try {
            // 실제 메서드 실행
            result = joinPoint.proceed();
        } catch (Exception e) {
            // 예외 발생 시 처리
            log.setResponseBody("Error: " + e.getMessage());
            throw e; // 예외를 다시 던지기
        }

        // 메서드 실행 후 로깅
        log.setResponseBody(result != null ? result.toString() : "No response");

        apiLogService.createLog(log);
        ApiLogContext.clear(); // Clear the context after logging

        return result;
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
        private static final ThreadLocal<ApiLogVO> apiLogThreadLocal = new ThreadLocal<>();

        public static void setLog(ApiLogVO log) {
            apiLogThreadLocal.set(log);
        }

        public static ApiLogVO getLog() {
            return apiLogThreadLocal.get();
        }

        public static void clear() {
            apiLogThreadLocal.remove();
        }
    }
}

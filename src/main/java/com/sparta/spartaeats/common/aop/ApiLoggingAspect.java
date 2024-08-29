package com.sparta.spartaeats.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.spartaeats.common.aop.domain.ApiLogVO;
import com.sparta.spartaeats.common.aop.service.ApiLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ApiLoggingAspect {

    // TODO securityContextHolder 에서 계정 정보 가져올 것
    private final ApiLogService apiLogService;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private HttpServletRequest request;

    @Before("@annotation(apiLogging)")
    public void logBefore(JoinPoint joinPoint, ApiLogging apiLogging) {
        ApiLogVO log = new ApiLogVO();
        log.setEndpoint(joinPoint.getSignature().toShortString());
        log.setHttpMethod(request.getMethod());
        log.setRequestBody(getRequestPayload(joinPoint));
        log.setCreatedAt(LocalDateTime.now());

        ApiLogContext.setLog(log);
    }

    @AfterReturning(pointcut = "@annotation(apiLogging)", returning = "result")
    public void logAfter(JoinPoint joinPoint, ApiLogging apiLogging, Object result) {
        ApiLogVO log = ApiLogContext.getLog();
        if (log != null) {
            // TODO 목록 조회의 경우 데이터가 너무 큼 수정필요
            log.setResponseBody(result != null ? result.toString() : "No response");
            apiLogService.createLog(log);
        }
    }

    private String getHttpMethod(JoinPoint joinPoint) {
        return Arrays.stream(joinPoint.getSignature().getDeclaringType().getAnnotations())
                .filter(annotation -> annotation instanceof RequestMapping)
                .map(annotation -> ((RequestMapping) annotation).method())
                .findFirst()
                .map(methods -> methods.length > 0 ? methods[0].name() : "UNKNOWN")
                .orElse("UNKNOWN");
    }

    private String getRequestPayload(JoinPoint joinPoint) {
        try {
            return objectMapper.writeValueAsString(joinPoint.getArgs());
        } catch (Exception e) {
            log.error("Error serializing request payload", e);
            return "ERROR";
        }
    }

    public class ApiLogContext {

        // ThreadLocal 변수를 사용하여 스레드 별로 독립적인 ApiLogVO 객체를 저장
        private static final ThreadLocal<ApiLogVO> apiLogThreadLocal = new ThreadLocal<>();

        // 로그 객체를 현재 스레드의 ThreadLocal에 저장
        public static void setLog(ApiLogVO log) {
            apiLogThreadLocal.set(log);
        }

        // 현재 스레드의 ThreadLocal에서 로그 객체를 가져옴
        public static ApiLogVO getLog() {
            return apiLogThreadLocal.get();
        }

        // ThreadLocal에서 로그 객체를 제거
        public static void clear() {
            apiLogThreadLocal.remove();
        }
    }
}

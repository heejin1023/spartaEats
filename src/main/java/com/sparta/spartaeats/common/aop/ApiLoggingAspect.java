package com.sparta.spartaeats.common.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sparta.spartaeats.apiLog.domain.ApiLog;
import com.sparta.spartaeats.apiLog.service.ApiLogService;
import com.sparta.spartaeats.common.security.UserDetailsImpl;
import com.sparta.spartaeats.user.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ApiLoggingAspect {

    private final ApiLogService apiLogService;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final HttpServletRequest request;

    private final HttpServletResponse response;

    @Around("@annotation(apiLogging)")
    public Object logAround(ProceedingJoinPoint joinPoint, ApiLogging apiLogging) throws Throwable {
        ApiLog log = new ApiLog();

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

        Signature signature = joinPoint.getSignature();
        String methodName = ((MethodSignature) signature).getMethod().getName();
        log.setApiName(methodName);

        // Request Body 및 PathVariable 추출 로직
        Object[] args = joinPoint.getArgs();
        Parameter[] parameters = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (parameter.isAnnotationPresent(RequestBody.class)) {
                try {
                    // @RequestBody 객체를 JSON 문자열로 변환
                    log.setRequestBody(objectMapper.writeValueAsString(args[i]));
                } catch (JsonProcessingException e) {
                    log.setRequestBody("Error processing request body: " + e.getMessage());
                }
            } else if (parameter.isAnnotationPresent(PathVariable.class)) {
                // @PathVariable 처리
                PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
                String pathVariableName = pathVariable.value();
                // PathVariable을 URL에서 추출 (단, URL을 직접 파싱하는 방법이 필요할 수 있음)
                // 경로 변수에 대한 값을 요청 매핑에서 추출하여 추가
                log.setRequestBody(log.getRequestBody() + " PathVariable - " + pathVariableName + ": " + args[i]);
            }
        }

        // RequestBody가 없는 경우 기존 로직 사용
        if (log.getRequestBody() == null || log.getRequestBody().isEmpty()) {
            log.setRequestBody(getRequestPayload(cachedRequest));
        }

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
        log.setResponseCode(String.valueOf(response.getStatus()));
        apiLogService.createLog(log);
        ApiLogContext.clear(); // Clear the context after logging

        return result;
    }

    private String getRequestPayload(HttpServletRequest request) {
        StringBuilder payload = new StringBuilder();

        // 요청 본문 읽기
        if (request instanceof ContentCachingRequestWrapper) {
            ContentCachingRequestWrapper cachedRequest = (ContentCachingRequestWrapper) request;
            byte[] body = cachedRequest.getContentAsByteArray();
            if (body.length > 0) {
                try {
                    String encoding = cachedRequest.getCharacterEncoding();
                    if (encoding == null) {
                        encoding = "UTF-8"; // 기본 문자 인코딩
                    }
                    String requestBody = new String(body, encoding);

                    // JSON 객체로 변환 및 빈 필드 필터링
                    JsonNode rootNode = objectMapper.readTree(requestBody);
                    JsonNode filteredNode = filterEmptyFields(rootNode);

                    // 필터링된 JSON 문자열로 변환
                    payload.append(objectMapper.writeValueAsString(filteredNode));

                } catch (UnsupportedEncodingException | JsonProcessingException e) {
                    log.error("Error processing request payload", e);
                }
            }
        }

        // 요청 파라미터 읽기
        StringBuilder params = new StringBuilder();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();
            if (values != null && values.length > 0) {
                params.append(key).append("=").append(String.join(",", values)).append("&");
            }
        }
        if (params.length() > 0) {
            params.setLength(params.length() - 1); // 마지막 '&' 제거
            payload.append(" RequestParams: ").append(params.toString());
        }

        return payload.toString();
    }

    private JsonNode filterEmptyFields(JsonNode node) {
        if (node == null) {
            return null;
        }

        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                JsonNode value = field.getValue();
                JsonNode filteredValue = filterEmptyFields(value);
                if (filteredValue == null || (filteredValue.isObject() && !filteredValue.fields().hasNext())) {
                    fields.remove();
                } else {
                    objectNode.set(field.getKey(), filteredValue);
                }
            }
        } else if (node.isArray()) {
            ArrayNode arrayNode = (ArrayNode) node;
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode item = arrayNode.get(i);
                JsonNode filteredItem = filterEmptyFields(item);
                if (filteredItem == null || (filteredItem.isObject() && !filteredItem.fields().hasNext())) {
                    arrayNode.remove(i);
                    i--; // Adjust index after removal
                } else {
                    arrayNode.set(i, filteredItem);
                }
            }
        }
        return node;
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

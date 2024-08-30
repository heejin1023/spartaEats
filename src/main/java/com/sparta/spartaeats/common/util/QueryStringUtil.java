package com.sparta.spartaeats.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.stream.Collectors;

public class QueryStringUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String convertToQueryString(Object requestBody) {
        try {
            // 요청 본문을 Map으로 변환
            Map<String, Object> map = objectMapper.convertValue(requestBody, Map.class);

            // Map을 쿼리 문자열로 변환
            MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                queryParams.add(entry.getKey(), entry.getValue().toString());
            }

            return queryParams.entrySet().stream()
                    .flatMap(e -> e.getValue().stream().map(v -> e.getKey() + "=" + v))
                    .collect(Collectors.joining("&"));
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

}

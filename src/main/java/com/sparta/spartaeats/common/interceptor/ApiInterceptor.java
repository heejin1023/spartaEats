package com.sparta.spartaeats.common.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.spartaeats.common.model.ApiResult;
import com.sparta.spartaeats.common.type.ApiResultError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Slf4j
public class ApiInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        log.debug("===============APII BEGIN ====================");
        log.debug("Request URI ===> " + request.getRequestURI());

        HttpSession session = request.getSession();
        // 	//로그인 정보 누락
        // 	logger.debug("== error:9201");
        // 	axError(request, response, ApiResultError.ERROR_INVALID_LOGIN);
        // 	return false;
        // }

        return true;
    }

    /**
     * API 오류 처리
     * @param request
     * @param response
     * @param code
     */
    private void axError(HttpServletRequest request, HttpServletResponse response, ApiResultError code) {
        ObjectMapper mapper = new ObjectMapper();

        response.setContentType("application/json;charset=utf-8");

        try {
            ApiResult axRet = new ApiResult(code);
            mapper.writeValue(response.getWriter(), axRet);
        } catch (IOException e) {
            log.error("axError Write Error!", e);
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {

        log.debug("================APII END ======================");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object,
                                @Nullable Exception arg3) throws Exception {
    }

}

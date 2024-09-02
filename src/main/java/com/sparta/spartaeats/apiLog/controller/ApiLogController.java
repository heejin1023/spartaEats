package com.sparta.spartaeats.apiLog.controller;

import com.sparta.spartaeats.apiLog.domain.ApiLog;
import com.sparta.spartaeats.apiLog.dto.ApiLogSearchCondition;
import com.sparta.spartaeats.apiLog.service.ApiLogService;
import com.sparta.spartaeats.common.aop.ApiLogging;
import com.sparta.spartaeats.common.controller.CustomApiController;
import com.sparta.spartaeats.common.model.ApiResult;
import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.common.type.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/apilogs")
@RequiredArgsConstructor
public class ApiLogController extends CustomApiController {

    private final ApiLogService apiLogService;

    /**
     * Api Log 목록조회
     * @param sc
     * @return
     */
    @ApiLogging
    @Secured({UserRoleEnum.Authority.OWNER, UserRoleEnum.Authority.ADMIN})
    @GetMapping
    @RequestMapping
    public ApiResult getApiLogList(ApiLogSearchCondition sc) {
        ApiResult apiResult = new ApiResult();

        sc.validateAndSetDefaults();;
        Pageable pageable = sc.generatePageable();
        Page<ApiLog> apiLogList = apiLogService.getApiLogList(sc, pageable);

        if(apiLogList != null){
            apiResult.set(ApiResultError.NO_ERROR).setList(apiLogList).setPageInfo(apiLogList);
        }
        return apiResult;
    }
}

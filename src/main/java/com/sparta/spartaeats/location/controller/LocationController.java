package com.sparta.spartaeats.location.controller;

import com.sparta.spartaeats.address.dto.AddressResponseDto;
import com.sparta.spartaeats.common.aop.ApiLogging;
import com.sparta.spartaeats.common.controller.CustomApiController;
import com.sparta.spartaeats.common.model.ApiResult;
import com.sparta.spartaeats.common.security.UserDetailsImpl;
import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.location.dto.LocationRequestDto;
import com.sparta.spartaeats.location.dto.LocationResponseDto;
import com.sparta.spartaeats.location.service.LocationService;
import com.sparta.spartaeats.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store_areas")
@Slf4j
public class LocationController extends CustomApiController {

    private final LocationService locationService;

    //지역 등록
    @ApiLogging
    @PostMapping
    public ApiResult createLocation(@RequestBody LocationRequestDto requestDto,
                                    @AuthenticationPrincipal UserDetailsImpl userDetails,
                                    Errors errors) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        if (errors.hasErrors()) {
            return bindError(errors, apiResult);
        }
        LocationResponseDto responseDto = locationService.createLocation(requestDto, userDetails.getUser());
        apiResult.set(ApiResultError.NO_ERROR).setResultData(responseDto);
        return apiResult;
    }

    // 지역 수정
    @ApiLogging
    @PatchMapping("/{locationId}")
    public ApiResult updateLocation(@PathVariable UUID locationId,
                                              @RequestBody LocationRequestDto requestDto,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails,
                                              Errors errors) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        if (errors.hasErrors()) {
            return bindError(errors, apiResult);
        }
        LocationResponseDto responseDto = locationService.updateLocation(locationId, requestDto, userDetails.getUser());
        apiResult.set(ApiResultError.NO_ERROR).setResultData(responseDto);
        return apiResult;
    }

    //지역 삭제
    @ApiLogging
    @PatchMapping("/{locationId}/delete")
    public ApiResult deleteLocation(@PathVariable UUID locationId,
                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        locationService.deleteLocation(locationId, user);
        ApiResult apiResult = new ApiResult(ApiResultError.NO_ERROR);

        return apiResult;
    }

    //지역코드 목록 조회
    @ApiLogging
    @GetMapping
    public ApiResult getAllLocations(
            @RequestParam int pageNumber,
            @RequestParam int pageSize) {
        if (pageSize != 10 && pageSize != 30 && pageSize != 50) {
            throw new IllegalArgumentException("pageSize는 10, 30, 50 중 하나여야 합니다.");
        }
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<LocationResponseDto> data = (Page<LocationResponseDto>) locationService.getAllLocations(pageable);

        apiResult.set(ApiResultError.NO_ERROR).setList(data).setPageInfo(data);
        return apiResult;
    }
}

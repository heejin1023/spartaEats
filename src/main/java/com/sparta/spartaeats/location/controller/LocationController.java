package com.sparta.spartaeats.location.controller;

import com.sparta.spartaeats.address.dto.AddressResponseDto;
import com.sparta.spartaeats.common.aop.ApiLogging;
import com.sparta.spartaeats.common.model.ApiResult;
import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.location.dto.LocationRequestDto;
import com.sparta.spartaeats.location.dto.LocationResponseDto;
import com.sparta.spartaeats.entity.*;
import com.sparta.spartaeats.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store_areas")
@Slf4j
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public LocationResponseDto createLocation(@RequestBody LocationRequestDto requestDto) {
        return locationService.createLocation(requestDto);
    }

    @PatchMapping("/{locationId}")
    public LocationResponseDto updateLocation(@PathVariable UUID locationId, @RequestBody LocationRequestDto requestDto) {
        return locationService.updateLocation(locationId, requestDto);
    }

    @DeleteMapping("/{locationId}/delete")
    public void deleteLocation(@PathVariable UUID locationId) {
        locationService.deleteLocation(locationId);
    }

    //지역코드 목록 조회
    @ApiLogging
    @GetMapping
    public ApiResult getAllLocations(
            @RequestParam int pageNumber,
            @RequestParam int pageSize) {

        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<LocationResponseDto> data = (Page<LocationResponseDto>) locationService.getAllLocations(pageable);

        apiResult.set(ApiResultError.NO_ERROR).setList(data).setPageInfo(data);
        return apiResult;
    }
}

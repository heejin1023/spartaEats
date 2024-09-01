package com.sparta.spartaeats.address.controller;

import com.sparta.spartaeats.address.service.AddressService;
import com.sparta.spartaeats.address.dto.AddressRequestDto;
import com.sparta.spartaeats.address.dto.AddressResponseDto;
import com.sparta.spartaeats.common.aop.ApiLogging;
import com.sparta.spartaeats.common.controller.CustomApiController;
import com.sparta.spartaeats.common.jwt.JwtUtil;
import com.sparta.spartaeats.common.model.ApiResult;
import com.sparta.spartaeats.common.security.UserDetailsImpl;
import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.common.type.UserRoleEnum;
import com.sparta.spartaeats.user.domain.User;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
@Slf4j
public class AddressController extends CustomApiController {

    private final AddressService addressService;

    // 배송지 등록
    @ApiLogging
    @PostMapping
    public ApiResult createAddress(@RequestBody AddressRequestDto addressRequestDto,
                                   @AuthenticationPrincipal UserDetailsImpl userDetails,
                                   Errors errors) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        if (errors.hasErrors()) {
            return bindError(errors, apiResult);
        }
        AddressResponseDto responseDto = addressService.createAddress(addressRequestDto, userDetails.getUser());
        apiResult.set(ApiResultError.NO_ERROR).setResultData(responseDto);
        return apiResult;
    }

    // 배송지 수정
    @ApiLogging
    @PatchMapping("/{addressId}")
    public ApiResult updateAddress(@PathVariable String addressId,
                                   @RequestBody AddressRequestDto addressRequestDto,
                                   @AuthenticationPrincipal UserDetailsImpl userDetails,
                                   Errors errors) {

        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        if (errors.hasErrors()) {
            return bindError(errors, apiResult);
        }
        Long userIdx = userDetails.getUser().getId(); // User 객체에서 userIdx 가져오기
        AddressResponseDto responseDto = addressService.updateAddress(UUID.fromString(addressId), addressRequestDto, userDetails.getUser());
        apiResult.set(ApiResultError.NO_ERROR).setResultData(responseDto);
        return apiResult;
    }

    // 배송지 삭제
    @ApiLogging
    @PatchMapping("/{addressId}/delete")
    public ApiResult deleteAddress(@PathVariable String addressId,
                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        addressService.deleteAddress(UUID.fromString(addressId), user);
        ApiResult apiResult = new ApiResult(ApiResultError.NO_ERROR);

        return apiResult;
    }

    // 배송지 상세 조회
    @ApiLogging
    @GetMapping("/{addressId}")
    public ApiResult getAddressById(@PathVariable String addressId,
                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Long userIdx = userDetails.getUser().getId(); // User 객체에서 userIdx 가져오기
        AddressResponseDto responseDto = addressService.getAddressById(UUID.fromString(addressId), userIdx);
        ApiResult apiResult = new ApiResult(ApiResultError.NO_ERROR).setResultData(responseDto);

        return apiResult;
    }

    // 배송지 목록 조회 (페이징)
    @ApiLogging
    @GetMapping
    public ApiResult getAddresses(
            @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "local", required = false) String local,
            @RequestParam(value = "orderId", required = false) Long orderId,
            @RequestParam(value = "useYn", required = false) Character useYn,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        User user = userDetails.getUser();
        Long userIdx = userDetails.getUser().getId(); // User 객체에서 userIdx 가져오기
        UserRoleEnum role = userDetails.getUser().getUserRole(); // Role 가져오기

        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        Page<AddressResponseDto> data = addressService.getAddresses(pageable, userIdx, role, local, orderId, useYn);

        apiResult.set(ApiResultError.NO_ERROR).setList(data).setPageInfo(data);

        return apiResult;
    }
}

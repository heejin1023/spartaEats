package com.sparta.spartaeats.order.controller;


import com.sparta.spartaeats.common.aop.ApiLogging;
import com.sparta.spartaeats.common.controller.CustomApiController;
import com.sparta.spartaeats.common.model.ApiResult;
import com.sparta.spartaeats.common.security.UserDetailsImpl;
import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.common.type.UserRoleEnum;
import com.sparta.spartaeats.responseDto.MultiResponseDto;
import com.sparta.spartaeats.responseDto.SimpleResponseDto;
import com.sparta.spartaeats.responseDto.SingleResponseDto;
import com.sparta.spartaeats.order.dto.OrderRequestDto;
import com.sparta.spartaeats.order.dto.OrderSearchCondition;
import com.sparta.spartaeats.order.dto.UpdateOrderDto;
import com.sparta.spartaeats.order.service.OrderService;
import com.sparta.spartaeats.user.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/orders")
@RestController
@Slf4j
@RequiredArgsConstructor
public class OrderController extends CustomApiController {

    private final OrderService orderService;

    @Secured(UserRoleEnum.Authority.USER)
    @ApiLogging
    @PostMapping
    public ApiResult order(@Valid @RequestBody OrderRequestDto orderRequestDto, Errors errors, @AuthenticationPrincipal UserDetailsImpl userDetails){ //
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        if(errors.hasErrors()) {
            return bindError(errors,apiResult);
        }
        User user = userDetails.getUser();
        SingleResponseDto responseDto = orderService.order(orderRequestDto,user); // ,userDetails.getUser()
        return new ApiResult().set(responseDto.getResultCode()).setResultData(responseDto.getData()).setResultMessage(responseDto.getResultMessage());
    }

    @Secured({UserRoleEnum.Authority.OWNER, UserRoleEnum.Authority.ADMIN})
    @ApiLogging
    @PatchMapping("/{orderId}")
    public ApiResult updateOrderStatus(@PathVariable UUID orderId, @Valid @RequestBody UpdateOrderDto requestDto, Errors errors) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        if (errors.hasErrors()) { //파라미터 바인딩 오류시 리턴
            return bindError(errors, apiResult);
        }
        SingleResponseDto responseDto = orderService.updateOrder(orderId, requestDto);
        return new ApiResult().set(responseDto.getResultCode(),responseDto.getResultMessage()).setResultData(responseDto.getData());
    }

    @Secured(UserRoleEnum.Authority.USER)
    @ApiLogging
    @PostMapping("/cancel/{orderId}")
    public ApiResult cancelOrder(@PathVariable UUID orderId) {

        SimpleResponseDto responseDto = orderService.cancelOrder(orderId);
//        return new SimpleResponseDto(ApiResultError.NO_ERROR, "주문이 취소되었습니다");
        return new ApiResult().set(responseDto.getResultCode(), responseDto.getResultMessage());
    }


    @GetMapping("/{orderId}")
    @ApiLogging
    public ApiResult getOneOrder(@PathVariable UUID orderId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        SingleResponseDto responseDto = orderService.getOneOrder(orderId,user);
        return new ApiResult().set(responseDto.getResultCode(), responseDto.getResultMessage()).setResultData(responseDto.getData());
    }

    @ApiLogging
    @GetMapping
    public MultiResponseDto getOrderList(OrderSearchCondition cond, Pageable pageable,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        System.out.println("user.getUserId() = " + user.getUserId());
        MultiResponseDto responseDto = orderService.getOrderList(cond, pageable,user);
        return responseDto;
    }

    @ApiLogging
    @DeleteMapping("/{orderId}")
    public ApiResult deleteOrder(@PathVariable UUID orderId) {
        User user = getLoginedUserObject();
        Long userId = user.getId();
        SimpleResponseDto responseDto = orderService.deleteOrder(orderId);
        return new ApiResult().set(responseDto.getResultCode(), responseDto.getResultMessage());
    }

}

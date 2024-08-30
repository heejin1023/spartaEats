package com.sparta.spartaeats.order.controller;

//import com.sparta.spartaeats.UserDetailsImpl;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/orders")
@RestController
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Secured(UserRoleEnum.Authority.USER)
    @PostMapping
    public ApiResult order(@RequestBody OrderRequestDto orderRequestDto) throws Exception { //, @AuthenticationPrincipal UserDetailsImpl userDetails
        SingleResponseDto responseDto = orderService.order(orderRequestDto); // ,userDetails.getUser()
        return new ApiResult().set(responseDto.getResultCode()).setResultData(responseDto.getData()).setResultMessage(responseDto.getResultMessage());
    }

    @Secured({UserRoleEnum.Authority.OWNER, UserRoleEnum.Authority.ADMIN})
    @PatchMapping("/{orderId}")
    public ApiResult updateOrderStatus(@PathVariable UUID orderId, @RequestBody UpdateOrderDto requestDto) {
        SingleResponseDto responseDto = orderService.updateOrder(orderId, requestDto);
        return new ApiResult().set(responseDto.getResultCode(),responseDto.getResultMessage()).setResultData(responseDto.getData());
    }

    @Secured(UserRoleEnum.Authority.USER)
    @PostMapping("/cancel/{orderId}")
    public ApiResult cancelOrder(@PathVariable UUID orderId) {
        SimpleResponseDto responseDto = orderService.cancelOrder(orderId);
//        return new SimpleResponseDto(ApiResultError.NO_ERROR, "주문이 취소되었습니다");
        return new ApiResult().set(responseDto.getResultCode(), responseDto.getResultMessage());
    }


    @GetMapping("/{orderId}")
    public ApiResult getOneOrder(@PathVariable UUID orderId,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String userRole = userDetails.getUser().getUserRole();
        SingleResponseDto responseDto = orderService.getOneOrder(orderId,userRole);
        return new ApiResult().set(responseDto.getResultCode(), responseDto.getResultMessage()).setResultData(responseDto.getData());
    }

    @GetMapping
    public ApiResult getOrderList(OrderSearchCondition cond, Pageable pageable) {
        MultiResponseDto responseDto = orderService.getOrderList(cond, pageable);
        return new ApiResult().set(responseDto.getResultCode(), responseDto.getResultMessage()).setList(responseDto.getResultData()).setPageInfo(responseDto.getResultData()).setSc(cond);
    }


    @DeleteMapping("/{orderId}")
    public ApiResult deleteOrder(@PathVariable UUID orderId) {
        SimpleResponseDto responseDto = orderService.deleteOrder(orderId);
        return new ApiResult().set(responseDto.getResultCode(), responseDto.getResultMessage());
    }

}

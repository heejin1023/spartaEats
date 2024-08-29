package com.sparta.spartaeats.order.controller;

//import com.sparta.spartaeats.UserDetailsImpl;

import com.sparta.spartaeats.common.type.ApiResultError;
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
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/orders")
@RestController
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<SingleResponseDto> order(@RequestBody OrderRequestDto orderRequestDto) throws Exception { //, @AuthenticationPrincipal UserDetailsImpl userDetails
        SingleResponseDto responseDto = orderService.order(orderRequestDto); // ,userDetails.getUser()
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<SingleResponseDto> updateOrderStatus(@PathVariable UUID orderId, @RequestBody UpdateOrderDto requestDto) {
        SingleResponseDto responseDto = orderService.updateOrder(orderId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/cancel/{orderId}")
    public SimpleResponseDto cancelOrder(@PathVariable UUID orderId) {
        SimpleResponseDto responseDto = orderService.cancelOrder(orderId);
        return new SimpleResponseDto(ApiResultError.NO_ERROR.getCode(), "주문이 취소되었습니다");
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<SingleResponseDto> getOneOrder(@PathVariable UUID orderId) {
        SingleResponseDto responseDto = orderService.getOneOrder(orderId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public MultiResponseDto getOrderList(OrderSearchCondition cond, Pageable pageable) {
        return orderService.getOrderList(cond, pageable);
    }

    @DeleteMapping("/{orderId}")
    public SimpleResponseDto deleteOrder(@PathVariable UUID orderId) {
        SimpleResponseDto responseDto = orderService.deleteOrder(orderId);
        return responseDto;
    }

}

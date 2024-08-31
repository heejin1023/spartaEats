package com.sparta.spartaeats.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.spartaeats.order.domain.Order;
import com.sparta.spartaeats.common.type.OrderStatus;
import com.sparta.spartaeats.common.type.OrderType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class OrderListResponseDto {

    private UUID orderId;
    private Long userId;
    private UUID storeId;
    private Integer orderPrice;
    private String memo;
    private OrderStatus orderStatus;
    private OrderType orderType;
    private List<OrderProductDto> orderProducts;

    @QueryProjection
    public OrderListResponseDto(UUID orderId, Long userId, UUID storeId, Integer orderPrice, String memo, OrderStatus orderStatus, OrderType orderType, List<OrderProductDto> orderProducts) {
        this.orderId = orderId;
        this.userId = userId;
        this.storeId = storeId;
        this.orderPrice = orderPrice;
        this.memo = memo;
        this.orderStatus = orderStatus;
        this.orderType = orderType;
        this.orderProducts = orderProducts;
    }

    public OrderListResponseDto(Order order, List<OrderProductDto> list) {
        this.orderId = order.getId();
        this.userId = order.getUser().getId();
        this.storeId = order.getStore().getId();
        this.orderPrice = order.getOrderPrice();
        this.memo = order.getMemo();
        this.orderStatus = order.getOrderStatus();
        this.orderType = order.getOrderType();
        orderProducts = list;
    }
}

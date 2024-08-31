package com.sparta.spartaeats.order.dto;

import com.sparta.spartaeats.common.type.OrderType;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class OrderRequestDto {

    private OrderType orderType;
    private UUID deliveryId;
    private String memo;
    private UUID storeId;
    private List<OrderProductDto> orderProducts;


}

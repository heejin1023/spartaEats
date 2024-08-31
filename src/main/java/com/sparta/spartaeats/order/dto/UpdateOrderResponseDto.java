package com.sparta.spartaeats.order.dto;

import com.sparta.spartaeats.common.type.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UpdateOrderResponseDto {
    private UUID orderId;
    private OrderStatus orderStatus;
}

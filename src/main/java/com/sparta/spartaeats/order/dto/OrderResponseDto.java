package com.sparta.spartaeats.order.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OrderResponseDto {

    private UUID orderId;
    private UUID storeId;
    private Integer orderPrice;
    private Long userId;
    private LocalDateTime createdAt;
}

package com.sparta.spartaeats.order.dto;

import com.sparta.spartaeats.common.type.OrderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class OrderRequestDto {

    private OrderType orderType;
    private UUID deliveryId;
    private String memo;
    @NotNull
    private UUID storeId;
    @NotEmpty
    private List<OrderProductDto> orderProducts;


}

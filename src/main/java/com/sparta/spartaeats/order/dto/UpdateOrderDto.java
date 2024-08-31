package com.sparta.spartaeats.order.dto;

import com.sparta.spartaeats.common.type.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class UpdateOrderDto {
    private OrderStatus orderStatus;
}

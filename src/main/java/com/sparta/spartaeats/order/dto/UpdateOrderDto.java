package com.sparta.spartaeats.order.dto;

import com.sparta.spartaeats.common.type.OrderStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class UpdateOrderDto {
    @NotNull
    private OrderStatus orderStatus;
}

package com.sparta.spartaeats.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class OrderProductDto {

    private UUID productId;
    private Integer amount;
    private Integer price;

    @QueryProjection
    public OrderProductDto(UUID productId, Integer amount, Integer price) {
        this.productId = productId;
        this.amount = amount;
        this.price = price;
    }
}

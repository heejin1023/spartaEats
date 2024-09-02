package com.sparta.spartaeats.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ProductResponseDto {

    private UUID id;
    private UUID store_id;
    private String productName;
    private Integer price;
    private String productDescription;
    private Character use_yn;
}

package com.sparta.spartaeats.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductSearchRequestDto {
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("store_name")
    private String storeName;
    @JsonProperty("use_yn")
    private Character useYn;
}

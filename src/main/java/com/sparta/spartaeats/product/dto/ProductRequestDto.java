package com.sparta.spartaeats.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Data
@Getter
public class ProductRequestDto {
    @JsonProperty("store_id")
    private UUID storeId;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("price")
    private Integer price;
    @JsonProperty("product_description")
    private String productDescription;
    @JsonProperty("use_yn")
    private String useYn;
}

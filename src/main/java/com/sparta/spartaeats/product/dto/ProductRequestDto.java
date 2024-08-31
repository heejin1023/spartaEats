package com.sparta.spartaeats.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.spartaeats.product.domain.validationGroup.ValidProduct001;
import com.sparta.spartaeats.user.domain.validationGroup.ValidUser0001;
import com.sparta.spartaeats.user.domain.validationGroup.ValidUser0002;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Data
@Getter
public class ProductRequestDto {

    @JsonProperty("store_id")
    private UUID storeId;

    @NotBlank(groups = {ValidProduct001.class},
            message = "상품명을 입력해 주세요.")
    @JsonProperty("product_name")
    private String productName;

    @NotBlank(groups = {ValidProduct001.class},
            message = "가격을 입력해 주세요.")
    @Pattern(regexp = "/^[0-9]$/",
            groups = {ValidProduct001.class,ValidProduct001.class},
            message = "가격은 숫자로 입력해주세요.")
    @JsonProperty("price")
    private Integer price;

    @Size(max = 1000,
        message = "1000자 이내로 입력하세요.")
    @JsonProperty("product_description")
    private String productDescription;

    @JsonProperty("use_yn")
    private String useYn;
}

package com.sparta.spartaeats.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.spartaeats.product.domain.validationGroup.ValidProduct001;
import com.sparta.spartaeats.product.domain.validationGroup.ValidProduct002;
import com.sparta.spartaeats.user.domain.validationGroup.ValidUser0001;
import com.sparta.spartaeats.user.domain.validationGroup.ValidUser0002;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Data
@Getter
public class ProductRequestDto {

    @NotNull(groups = {ValidProduct001.class},
            message = "store_id를 입력해 주세요.")
    @JsonProperty("store_id")
    private UUID storeId;

    @NotBlank(groups = {ValidProduct001.class},
            message = "상품명을 입력해 주세요.")
    @JsonProperty("product_name")
    private String productName;

    @NotNull(groups = {ValidProduct001.class},
            message = "가격을 입력해 주세요.")
    @JsonProperty("price")
    private Integer price;

    @Size(max = 1000,
        groups = {ValidProduct001.class, ValidProduct002.class},
        message = "1000자 이내로 입력하세요.")
    @JsonProperty("product_description")
    private String productDescription;

    @Pattern(regexp = "^(Y|N)$",
            groups = {ValidProduct002.class},
            message = "Y 또는 N 으로 입력하세요.")
    @JsonProperty("use_yn")
    private String useYn;
}

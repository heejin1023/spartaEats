package com.sparta.spartaeats.storeCategory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StoreCategorySearchRequestDto {
    @JsonProperty("category_name")
    private String categoryName;
    @JsonProperty("use_yn")
    private String useYn;

}

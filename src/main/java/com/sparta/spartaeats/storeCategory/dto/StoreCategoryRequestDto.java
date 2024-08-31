package com.sparta.spartaeats.storeCategory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreCategoryRequestDto {

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("category_description")
    private String categoryDescription;

    @JsonProperty("use_yn")
    private String useYn;
}

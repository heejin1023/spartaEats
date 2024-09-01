package com.sparta.spartaeats.storeCategory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.spartaeats.storeCategory.domain.validationGroup.ValidStoreCategory001;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreCategoryRequestDto {

    @NotBlank(groups = {ValidStoreCategory001.class},
            message = "카테고리명을 입력하세요.")
    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("category_description")
    private String categoryDescription;

    @JsonProperty("use_yn")
    private String useYn;
}

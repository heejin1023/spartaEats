package com.sparta.spartaeats.store_category;

import lombok.Getter;

@Getter
public class StoreCategorySearchRequestDto {
    private String categoryName;
    private String useYn;
    public StoreCategorySearchRequestDto(String categoryName, String useYn) {
        this.categoryName = categoryName;
        this.useYn = this.useYn;
    }
}

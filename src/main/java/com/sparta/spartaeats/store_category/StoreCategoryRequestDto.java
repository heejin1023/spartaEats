package com.sparta.spartaeats.store_category;

import lombok.*;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreCategoryRequestDto {
    private String category_name;
    private String category_description;
}

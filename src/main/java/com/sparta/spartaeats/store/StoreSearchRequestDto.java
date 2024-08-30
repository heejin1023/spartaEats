package com.sparta.spartaeats.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreSearchRequestDto {
    private String storeName;
    private String locationId;
    private String storeAddress;
    private String categoryId;
    private String useYn;
}

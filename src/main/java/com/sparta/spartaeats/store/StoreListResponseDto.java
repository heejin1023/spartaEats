package com.sparta.spartaeats.store;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreListResponseDto {
    private UUID storeId;
    private String storeName;
    private String categoryName;
}

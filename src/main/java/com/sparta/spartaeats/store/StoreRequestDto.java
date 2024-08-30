package com.sparta.spartaeats.store;

import lombok.*;

import java.util.UUID;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreRequestDto {
    private Long userIdx;
    private String storeName;
    private String storeContact;
    private String storeAddress;
    private UUID categoryId;
    private UUID locationId;
    private String useYn;
}

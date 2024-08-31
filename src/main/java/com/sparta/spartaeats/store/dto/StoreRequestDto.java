package com.sparta.spartaeats.store.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreRequestDto {
    @JsonProperty("store_id")
    private UUID storeId;
    @JsonProperty("user_idx")
    private Long userIdx;
    @JsonProperty("store_name")
    private String storeName;
    @JsonProperty("store_contact")
    private String storeContact;
    @JsonProperty("store_address")
    private String storeAddress;
    @JsonProperty("category_id")
    private UUID categoryId;
    @JsonProperty("location_id")
    private UUID locationId;
    @JsonProperty("use_yn")
    private String useYn;
}

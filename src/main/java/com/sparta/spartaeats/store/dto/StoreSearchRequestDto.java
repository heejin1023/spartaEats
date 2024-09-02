package com.sparta.spartaeats.store.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreSearchRequestDto {
    @JsonProperty("store_name")
    private String storeName;
    @JsonProperty("location_id")
    private String locationId;
    @JsonProperty("store_address")
    private String storeAddress;
    @JsonProperty("category_id")
    private String categoryId;
    @JsonProperty("use_yn")
    private Character useYn;

}

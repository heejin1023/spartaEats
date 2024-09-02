package com.sparta.spartaeats.store.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.spartaeats.store.domain.validationGroup.ValidStore001;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreRequestDto {

    @JsonProperty("user_idx")
    private Long userIdx;

    @NotBlank(groups = {ValidStore001.class},
            message = "음식점 이름을 입력하세요")
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
    private Character useYn;
}

package com.sparta.spartaeats.store.dto;

import com.sparta.spartaeats.user.domain.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreResponseDto {
    private UUID storeId;
    private Long userIdx;
    private String storeName;
    private String storeContact;
    private String storeAddress;
    private UUID storeCategoryId;
    private Character useYn;

    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime modifiedAt;
    private Long modifiedBy;
    private LocalDateTime deletedAt;
    private Long deletedBy;

}

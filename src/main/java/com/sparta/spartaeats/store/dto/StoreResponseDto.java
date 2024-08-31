package com.sparta.spartaeats.store.dto;

import com.sparta.spartaeats.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreResponseDto {
    private UUID storeId;
    private User owner;
    private String storeName;
    private String storeContact;
    private String storeAddress;
    private UUID storeCategoryId;
    private String useYn;

    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime modifiedAt;
    private Long modifiedBy;
    private LocalDateTime deletedAt;
    private Long deletedBy;

}

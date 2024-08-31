package com.sparta.spartaeats.store;

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
    private User owner;
    private String storeName;
    private String storeContact;
    private String storeAddress;
    private UUID storeCategoryId;

    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime modifiedAt;
    private Long modifiedBy;
    private LocalDateTime deletedAt;
    private Long deletedBy;


    public StoreResponseDto(UUID storeId, String storeName, String storeContact, String storeAddress, UUID categoryId) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeContact = storeContact;
        this.storeAddress = storeAddress;
        this.storeCategoryId = categoryId;
    }
}

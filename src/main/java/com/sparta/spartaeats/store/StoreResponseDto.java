package com.sparta.spartaeats.store;

import com.sparta.spartaeats.entity.User;
import com.sparta.spartaeats.store_category.StoreCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

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

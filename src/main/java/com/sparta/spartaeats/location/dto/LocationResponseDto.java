package com.sparta.spartaeats.location.dto;

import com.sparta.spartaeats.entity.Product;
import com.sparta.spartaeats.location.entity.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class LocationResponseDto {
    private UUID locationId;
    private String locationName;
    private Character useYn;
    private Character delYn;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private LocalDateTime deletedAt;
    private Long deletedBy;

    public LocationResponseDto(Location location) {
        this.locationId = location.getId();
        this.locationName = location.getLocationName();
        this.useYn = location.getUseYn();
        this.delYn = location.getDelYn();
        this.createdAt = location.getCreatedAt();
        this.createdBy = location.getCreatedBy();
        this.updatedAt = location.getModifiedAt();
        this.updatedBy = location.getModifiedBy();
        this.deletedAt = location.getDeletedAt();
        this.deletedBy = location.getDeletedBy();
    }
}

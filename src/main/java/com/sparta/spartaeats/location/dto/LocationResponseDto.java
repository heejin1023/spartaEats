package com.sparta.spartaeats.location.dto;

import com.sparta.spartaeats.entity.*;
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

    public LocationResponseDto(Location location) {
        this.locationId = location.getId();
        this.locationName = location.getLocationName();
        this.useYn = location.getUseYn();
        this.delYn = location.getDelYn();
    }
}

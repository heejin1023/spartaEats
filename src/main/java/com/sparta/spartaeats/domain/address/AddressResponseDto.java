package com.sparta.spartaeats.domain.address;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class AddressResponseDto {
    private UUID delvrUuid;
    private Long userIdx;
    private String zip;
    private String local;
    private String address;
    private String address2;
    private String phone;
    private Character useYn;
    private Character delYn;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private LocalDateTime deletedAt;
    private String deletedBy;
}

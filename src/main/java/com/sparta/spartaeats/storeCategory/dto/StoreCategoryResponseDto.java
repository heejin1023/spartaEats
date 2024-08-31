package com.sparta.spartaeats.storeCategory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class StoreCategoryResponseDto {

    private UUID id;
    private String categoryName;
    private String categoryDescription;
    private String useYn;
    private String delYn;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime modifiedAt;
    private Long modifiedBy;
    protected LocalDateTime deletedAt;
    private Long deletedBy;
}

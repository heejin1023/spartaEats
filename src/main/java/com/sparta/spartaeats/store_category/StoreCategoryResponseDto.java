package com.sparta.spartaeats.store_category;

import com.sparta.spartaeats.store.Store;
import jakarta.persistence.*;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
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

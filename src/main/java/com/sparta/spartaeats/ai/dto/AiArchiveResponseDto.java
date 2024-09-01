package com.sparta.spartaeats.ai.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class AiArchiveResponseDto {

    private UUID aiId;
    private UUID productId;
    private String productName;
    private String requestContents;
    private String responseContents;
    private Character delYn;
    private LocalDateTime createdAt;
    private Long createdBy;
    private String createrName;

}

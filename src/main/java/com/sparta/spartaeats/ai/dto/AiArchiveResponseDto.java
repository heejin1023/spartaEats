package com.sparta.spartaeats.ai.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiArchiveResponseDto {

    private UUID aiId;
    private UUID productId;
    private String requestContents;
    private String responseContents;
    private Character delYn;
    private LocalDateTime createdAt;
    private Long createdBy;

    private String productName;
    private String userName;

    public AiArchiveResponseDto(UUID id, String requestContents,
                                String responseContents, Character delYn,
                                LocalDateTime createdAt,
                                String productName, String userName) {
        this.aiId = id;
        this.requestContents = requestContents;
        this.responseContents = responseContents;
        this.delYn = delYn;
        this.createdAt = createdAt;
        this.productName = productName;
        this.userName = userName;
    }
}

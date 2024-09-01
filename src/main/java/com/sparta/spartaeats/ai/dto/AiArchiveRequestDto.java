package com.sparta.spartaeats.ai.dto;

import com.sparta.spartaeats.common.dto.BaseRequestDto;
import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiArchiveRequestDto extends BaseRequestDto {

    private String question;

    private UUID productId;


}

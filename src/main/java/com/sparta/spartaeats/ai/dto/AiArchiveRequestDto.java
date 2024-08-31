package com.sparta.spartaeats.ai.dto;

import lombok.*;
import java.util.UUID;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiArchiveRequestDto {

    private String question;

    private UUID productId;


}

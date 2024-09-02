package com.sparta.spartaeats.ai.dto;

import com.sparta.spartaeats.common.dto.BaseRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiArchiveRequestDto extends BaseRequestDto {

    @NotBlank(message = "질문은 필수 입력 사항입니다.")
    private String question;

    @NotNull(message = "productId는 필수 입력 사항입니다.")
    private UUID productId;


}

package com.sparta.spartaeats.responseDto;

import com.sparta.spartaeats.common.type.ApiResultError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleResponseDto {
    private ApiResultError resultCode;
    private String resultMessage;
}

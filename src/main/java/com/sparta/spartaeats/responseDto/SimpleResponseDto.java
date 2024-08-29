package com.sparta.spartaeats.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleResponseDto {
    private String resultCode;
    private String resultMessage;
}

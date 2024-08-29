package com.sparta.spartaeats.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleResponseDto<T> {
    private String resultCode;
    private String resultMessage;
    private T data;
}

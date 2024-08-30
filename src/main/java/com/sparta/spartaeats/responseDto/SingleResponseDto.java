package com.sparta.spartaeats.responseDto;

import com.sparta.spartaeats.common.type.ApiResultError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleResponseDto<T> {
    private ApiResultError resultCode;
    private String resultMessage;
    private T data;
}

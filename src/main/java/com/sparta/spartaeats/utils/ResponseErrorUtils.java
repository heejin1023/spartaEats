package com.sparta.spartaeats.utils;

import com.sparta.spartaeats.responseDto.MultiResponseDto;
import com.sparta.spartaeats.responseDto.SimpleResponseDto;
import com.sparta.spartaeats.responseDto.SingleResponseDto;

public class ResponseErrorUtils {
    public static <T> SingleResponseDto<T> handleErrorSingle(String resultError, String errorMessage) {
        return new SingleResponseDto<>(resultError, errorMessage , null);
    }

    public static <T> SimpleResponseDto handleErrorSimple(String resultError, String errorMessage) {
        return new SimpleResponseDto(resultError, errorMessage );
    }

    public static <T> MultiResponseDto<T> handleErrorMulti(String resultError, String errorMessage) {
        return new MultiResponseDto<>(resultError, errorMessage,null,null );
    }

}

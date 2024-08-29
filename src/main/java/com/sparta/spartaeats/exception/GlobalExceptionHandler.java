package com.sparta.spartaeats.exception;

import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.responseDto.SimpleResponseDto;
import com.sparta.spartaeats.utils.ResponseErrorUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public SimpleResponseDto handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseErrorUtils.handleErrorSimple(ApiResultError.ERROR_INVALID_ARGUMENT.getCode(), e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public SimpleResponseDto handleIllegalStateException(IllegalArgumentException e) {
        return ResponseErrorUtils.handleErrorSimple(ApiResultError.ERROR_INVALID_STATE.getCode(), e.getMessage());
    }

    @ExceptionHandler(EmptyDataException.class)
    public SimpleResponseDto handleEmptyDataException(IllegalArgumentException e) {
        return ResponseErrorUtils.handleErrorSimple(ApiResultError.ERROR_EMPTY_DATA.getCode(), e.getMessage());
    }

    @ExceptionHandler(DeletedProductException.class)
    public SimpleResponseDto handleDeletedProductException(DeletedProductException e) {
        return ResponseErrorUtils.handleErrorSimple(ApiResultError.ERROR_INVALID_STATE.getCode(), e.getMessage());
    }

    @ExceptionHandler(OrderTimeOutException.class)
    public SimpleResponseDto handleOrderTimeOutException(DeletedProductException e) {
        return ResponseErrorUtils.handleErrorSimple(ApiResultError.ERROR_TIMEOUT.getCode(), e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public SimpleResponseDto handleNoSuchElementException(NoSuchElementException e) {
        return ResponseErrorUtils.handleErrorSimple(ApiResultError.ERROR_INVALID_ARGUMENT.getCode(), e.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public SimpleResponseDto handleException(Exception e) {
        return ResponseErrorUtils.handleErrorSimple(ApiResultError.ERROR_DEFAULT.getCode(), e.getMessage());
    }

}

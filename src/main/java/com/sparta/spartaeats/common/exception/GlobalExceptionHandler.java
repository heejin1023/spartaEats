package com.sparta.spartaeats.common.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(IllegalArgumentException.class)
//    public ApiResult handleIllegalArgumentException(IllegalArgumentException e) {
//        return new ApiResult().set(ApiResultError.ERROR_INVALID_ARGUMENT, e.getMessage());
//    }
//
//    @ExceptionHandler(IllegalStateException.class)
//    public ApiResult handleIllegalStateException(IllegalArgumentException e) {
//        return new ApiResult().set(ApiResultError.ERROR_INVALID_STATE, e.getMessage());
//    }
//
//    @ExceptionHandler(EmptyDataException.class)
//    public ApiResult handleEmptyDataException(IllegalArgumentException e) {
//        return new ApiResult().set(ApiResultError.ERROR_EMPTY_DATA, e.getMessage());
//    }
//
//    @ExceptionHandler(DeletedProductException.class)
//    public ApiResult handleDeletedProductException(DeletedProductException e) {
//        return new ApiResult().set(ApiResultError.ERROR_INVALID_STATE, e.getMessage());
//    }
//
//    @ExceptionHandler(OrderTimeOutException.class)
//    public ApiResult handleOrderTimeOutException(DeletedProductException e) {
//        return new ApiResult().set(ApiResultError.ERROR_TIMEOUT, e.getMessage());
//    }
//
//    @ExceptionHandler(NoSuchElementException.class)
//    public ApiResult handleNoSuchElementException(NoSuchElementException e) {
//        return new ApiResult().set(ApiResultError.ERROR_INVALID_ARGUMENT, e.getMessage());
//    }

//    @ExceptionHandler(Exception.class)
//    public ApiResult handleException(Exception e) {
//        return new ApiResult().set(ApiResultError.ERROR_DEFAULT, e.getMessage());
//    }

}

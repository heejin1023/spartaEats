package com.sparta.spartaeats.common.exception;

public class OrderTimeOutException extends RuntimeException{
    public OrderTimeOutException() {
        super();
    }

    public OrderTimeOutException(String message) {
        super(message);
    }

    public OrderTimeOutException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderTimeOutException(Throwable cause) {
        super(cause);
    }
}

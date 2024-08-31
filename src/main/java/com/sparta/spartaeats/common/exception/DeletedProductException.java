package com.sparta.spartaeats.common.exception;

public class DeletedProductException extends RuntimeException {
    public DeletedProductException() {
        super();
    }

    public DeletedProductException(String message) {
        super(message);
    }

    public DeletedProductException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeletedProductException(Throwable cause) {
        super(cause);
    }
}

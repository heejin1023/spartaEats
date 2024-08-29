package com.sparta.spartaeats.order.dto;

public class OrderResult<T> {
    private T data;
    private int pageNumber;
    private int offset;
    private int totalPages;
    private int totalElements;
    private int size;
}

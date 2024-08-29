package com.sparta.spartaeats.order.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderSearchCondition {

    private String productName;
    private String storeName;
    private String category;
    private String username;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}

package com.sparta.spartaeats.order.dto;

import lombok.Data;

@Data
public class OrderSearchCondition {

    private String productName;
    private String storeName;
    private String category;
    private String username;
}

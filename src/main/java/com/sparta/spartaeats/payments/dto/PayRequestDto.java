package com.sparta.spartaeats.payments.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PayRequestDto {

    private UUID orderId;
    private String pgType;
}

package com.sparta.spartaeats.payments.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class PayRequestDto {

    @NotBlank
    private UUID orderId;
    @NotBlank
    private String pgType;
}

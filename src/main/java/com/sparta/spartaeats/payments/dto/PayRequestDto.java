package com.sparta.spartaeats.payments.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PayRequestDto {

    @NotNull
    private UUID orderId;
    @NotBlank
    private String pgType;
}

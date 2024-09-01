package com.sparta.spartaeats.payments.dto;

import com.sparta.spartaeats.common.type.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PayUpdateRequestDto {
    @NotBlank
    private PaymentStatus status;
}

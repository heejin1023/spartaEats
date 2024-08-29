package com.sparta.spartaeats.payments.dto;

import com.sparta.spartaeats.types.PaymentStatus;
import lombok.Data;

@Data
public class PayUpdateRequestDto {
    private PaymentStatus status;
}

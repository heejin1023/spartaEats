package com.sparta.spartaeats.payments.dto;

import com.sparta.spartaeats.common.type.PaymentStatus;
import lombok.Data;

@Data
public class PayUpdateRequestDto {
    private PaymentStatus status;
}

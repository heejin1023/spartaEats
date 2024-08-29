package com.sparta.spartaeats.payments.dto;

import com.sparta.spartaeats.types.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PaymentUpdateResponseDto {

    private UUID paymentId;
    private PaymentStatus status;
}

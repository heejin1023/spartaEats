package com.sparta.spartaeats.payments.dto;

import com.sparta.spartaeats.common.type.PaymentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentSearchCond {

    private PaymentStatus status;
    private Long createdBy;
    private String pgType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}

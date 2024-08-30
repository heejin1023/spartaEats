package com.sparta.spartaeats.payments.repository;

import com.sparta.spartaeats.payments.dto.PaymentResponseDto;
import com.sparta.spartaeats.payments.dto.PaymentSearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentsRepositoryCustom {

    Page<PaymentResponseDto> findPaymentList(Pageable pageable, PaymentSearchCond cond);

}

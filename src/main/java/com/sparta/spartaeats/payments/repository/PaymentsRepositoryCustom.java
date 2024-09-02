package com.sparta.spartaeats.payments.repository;

import com.sparta.spartaeats.payments.dto.PaymentResponseDto;
import com.sparta.spartaeats.payments.dto.PaymentSearchCond;
import com.sparta.spartaeats.responseDto.MultiResponseDto;
import com.sparta.spartaeats.store.domain.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PaymentsRepositoryCustom {

    MultiResponseDto<PaymentResponseDto> findPaymentList(Pageable pageable, PaymentSearchCond cond);

    MultiResponseDto<PaymentResponseDto> findPaymentListWithUserRole(Pageable pageable, PaymentSearchCond cond, Long userId);

    MultiResponseDto<PaymentResponseDto> findPaymentListWithOwnerRole(Pageable pageable, PaymentSearchCond cond, UUID storeId);
}

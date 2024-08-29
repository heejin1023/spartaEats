package com.sparta.spartaeats.payments.controller;

import com.sparta.spartaeats.responseDto.MultiResponseDto;
import com.sparta.spartaeats.responseDto.SimpleResponseDto;
import com.sparta.spartaeats.responseDto.SingleResponseDto;
import com.sparta.spartaeats.payments.dto.*;
import com.sparta.spartaeats.payments.service.PaymentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentsController {

    private final PaymentsService paymentsService;

    @PostMapping
    public SingleResponseDto<PaymentResponseDto> payOrder(@RequestBody PayRequestDto payRequestDto) {
        return paymentsService.pay(payRequestDto);
    }

    @PatchMapping("/{paymentId}")
    public SingleResponseDto<PaymentUpdateResponseDto> updatePayment(@PathVariable UUID paymentId, @RequestBody PayUpdateRequestDto requestDto) {
        return paymentsService.updatePayment(paymentId, requestDto);
    }

    @DeleteMapping("/{paymentId}")
    public SimpleResponseDto deletePayment(@PathVariable UUID paymentId) {
        return paymentsService.deletePayment(paymentId);
    }

    @GetMapping("/{paymentId}")
    public SingleResponseDto<?> getOnePaymentResult(@PathVariable UUID paymentId) {
        return paymentsService.getOnePaymentResult(paymentId);
    }

    @GetMapping
    public MultiResponseDto<PaymentResponseDto> getAllPayments(Pageable pageable, PaymentSearchCond cond) {
        return paymentsService.getAllPayments(pageable, cond);
    }


}

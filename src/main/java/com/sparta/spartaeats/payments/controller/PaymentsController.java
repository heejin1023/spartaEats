package com.sparta.spartaeats.payments.controller;

import com.sparta.spartaeats.common.model.ApiResult;
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
    public ApiResult payOrder(@RequestBody PayRequestDto payRequestDto) {
        SingleResponseDto<PaymentResponseDto> responseDto = paymentsService.pay(payRequestDto);
        return new ApiResult().set(responseDto.getResultCode(), responseDto.getResultMessage()).setResultData(responseDto.getData());
    }

    @PatchMapping("/{paymentId}")
    public ApiResult updatePayment(@PathVariable UUID paymentId, @RequestBody PayUpdateRequestDto requestDto) {
        SingleResponseDto<PaymentUpdateResponseDto> responseDto = paymentsService.updatePayment(paymentId, requestDto);
        return new ApiResult().set(responseDto.getResultCode(), responseDto.getResultMessage()).setResultData(responseDto.getData());
    }

    @DeleteMapping("/{paymentId}")
    public ApiResult deletePayment(@PathVariable UUID paymentId) {
        SimpleResponseDto responseDto = paymentsService.deletePayment(paymentId);
        return new ApiResult().set(responseDto.getResultCode(), responseDto.getResultMessage());
    }

    @GetMapping("/{paymentId}")
    public ApiResult getOnePaymentResult(@PathVariable UUID paymentId) {
        SingleResponseDto<?> responseDto = paymentsService.getOnePaymentResult(paymentId);
        return new ApiResult().set(responseDto.getResultCode(), responseDto.getResultMessage()).setResultData(responseDto.getData());
    }

    @GetMapping
    public ApiResult getAllPayments(Pageable pageable, PaymentSearchCond cond) {
        MultiResponseDto<PaymentResponseDto> responseDto = paymentsService.getAllPayments(pageable, cond);
        return new ApiResult().set(responseDto.getResultCode(), responseDto.getResultMessage()).setResultData(responseDto.getResultData()).setPageInfo(responseDto.getResultData()).setSc(cond);
    }


}

package com.sparta.spartaeats.payments.service;

import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.responseDto.MultiResponseDto;
import com.sparta.spartaeats.responseDto.PageInfoDto;
import com.sparta.spartaeats.responseDto.SimpleResponseDto;
import com.sparta.spartaeats.responseDto.SingleResponseDto;
import com.sparta.spartaeats.order.domain.Order;
import com.sparta.spartaeats.payments.domain.Payment;
import com.sparta.spartaeats.common.exception.EmptyDataException;
import com.sparta.spartaeats.order.repository.OrderRepository;
import com.sparta.spartaeats.payments.dto.*;
import com.sparta.spartaeats.payments.repository.PaymentsRepository;
import com.sparta.spartaeats.common.type.OrderStatus;
import com.sparta.spartaeats.common.type.PaymentStatus;
import com.sparta.spartaeats.store.domain.Store;
import com.sparta.spartaeats.store.repository.StoreRepository;
import com.sparta.spartaeats.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PaymentsService {

    private final PaymentsRepository paymentsRepository;
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;

    public SingleResponseDto<PaymentResponseDto> pay(PayRequestDto payRequestDto) {
          Order order = orderRepository.findById(payRequestDto.getOrderId()).orElseThrow(
                () -> new IllegalArgumentException("order Not Found with orderId : " + payRequestDto.getOrderId()));
        if (order.getOrderPrice() <= 0) {
            throw new IllegalArgumentException("결제 금액이 0원입니다. orderId : " + payRequestDto.getOrderId());
        }

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("이미 결제 완료된 주문입니다. orderId : " + payRequestDto.getOrderId());
        }
            Payment payment = new Payment(order, order.getOrderPrice(), PaymentStatus.APPROVED, null, null, 'N', payRequestDto.getPgType() );
            paymentsRepository.save(payment);
            order.changeOrderStatus(order.getId(), OrderStatus.PREPARING);
            PaymentResponseDto paymentResponseDto = getPaymentResponseDto(payment);
            return new SingleResponseDto<>(ApiResultError.NO_ERROR, "결제가 완료되었습니다", paymentResponseDto);
    }

    public SingleResponseDto<PaymentUpdateResponseDto> updatePayment(UUID paymentId, PayUpdateRequestDto requestDto) {
            Payment payment = paymentsRepository.findByIdWithDel(paymentId).orElseThrow(() -> new IllegalArgumentException("payment Not Found with id : " + paymentId));
            if (payment.getPaymentStatus() == PaymentStatus.APPROVED) {
                throw new IllegalStateException("이미 결제 완료되었습니다.");
            }

            if (payment.getPaymentStatus() == PaymentStatus.CANCELED) {
                throw new IllegalStateException("취소된 결제는 상태를 변경할 수 없습니다");
            }

            payment.changeStatus(requestDto.getStatus());
            PaymentUpdateResponseDto responseDto = new PaymentUpdateResponseDto(paymentId, payment.getPaymentStatus());

            return new SingleResponseDto<>(ApiResultError.NO_ERROR, "결제 상태 업데이트 완료", responseDto);
    }

    public SimpleResponseDto deletePayment(UUID paymentId) {
            Payment payment = paymentsRepository.findByIdWithDel(paymentId).orElseThrow(() -> new IllegalArgumentException("payment Not Found with id : " + paymentId));
            if (payment.getDelYn() == 'Y') {
                throw new IllegalStateException("이미 삭제된 결제입니다");
            }
            payment.deletePayment();
            return new SimpleResponseDto(ApiResultError.NO_ERROR, "결제 내역이 삭제되었습니다.");
    }

    public SingleResponseDto<?> getOnePaymentResult(UUID paymentId, User user) {
        Long userId = user.getId();
        Payment payment = paymentsRepository.findByIdWithDelWithUser(paymentId,userId).orElseThrow(() -> new IllegalArgumentException("payment Not Found with id : " + paymentId));
        return new SingleResponseDto<>(ApiResultError.NO_ERROR, "결제 내역 단건 조회", getPaymentResponseDto(payment));
    }

    public MultiResponseDto<PaymentResponseDto> getAllPayments(Pageable pageable, PaymentSearchCond cond,User user) {
        Long userId = user.getId();
        String userRole = user.getUserRole();
        if (userRole.contains("CUSTOMER")) {
            return paymentsRepository.findPaymentListWithUserRole(pageable, cond, userId);
        } else if (userRole.contains("OWNER")) {
            Store store = storeRepository.findByOwner(user).orElseThrow(() -> new EmptyDataException("Not Found Store with Owner Id : " + userId));
            UUID storeId = store.getId();
            return paymentsRepository.findPaymentListWithOwnerRole(pageable, cond, storeId);
        } else {
            return paymentsRepository.findPaymentList(pageable, cond);
        }


    }

    private PaymentResponseDto getPaymentResponseDto(Payment payment) {
        return new PaymentResponseDto(payment.getId(), payment.getPayment_amount(), payment.getPaymentStatus());
    }


}

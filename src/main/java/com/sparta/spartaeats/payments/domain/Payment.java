package com.sparta.spartaeats.payments.domain;

import com.sparta.spartaeats.common.util.TimeStamped;
import com.sparta.spartaeats.order.domain.Order;
import com.sparta.spartaeats.common.type.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "p_payments")
@AllArgsConstructor @NoArgsConstructor
public class Payment extends TimeStamped {

    @Id
    @GeneratedValue
    @Column(name = "payment_id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private Integer payment_amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "payment_data", columnDefinition = "TEXT")
    private String paymentData;

    @Column(name = "payment_fail_data", columnDefinition = "TEXT")
    private String paymentFailData;

    private Character delYn;
    private String pgType;

    public Payment(Order order, Integer payment_amount, PaymentStatus paymentStatus, String paymentData, String paymentFailData, Character del_yn, String pgType) {
        this.order = order;
        this.payment_amount = payment_amount;
        this.paymentStatus = paymentStatus;
        this.paymentData = paymentData;
        this.paymentFailData = paymentFailData;
        this.delYn = del_yn;
        this.pgType = pgType;
    }

    public void changeStatus(PaymentStatus status) {
        this.paymentStatus = status;
    }

    public void deletePayment(Long userId) {
        this.deletedAt = LocalDateTime.now();
        this.setDeletedBy(userId);
        this.delYn = 'Y';
    }
}

package com.sparta.spartaeats.entity;

import com.sparta.spartaeats.types.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Getter
@Table(name = "p_payments")
public class Payment extends TimeStamped{

    @Id
    @GeneratedValue
    @Column(name = "payment_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Lob
    @Column(name = "payment_data", columnDefinition = "CLOB")
    private String paymentData;

    @Lob
    @Column(name = "payment_fail_data", columnDefinition = "CLOB")
    private String paymentFailData;

    private Character del_yn;
    private String pgType;

}

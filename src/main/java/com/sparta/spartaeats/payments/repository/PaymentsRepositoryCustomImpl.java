package com.sparta.spartaeats.payments.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.spartaeats.order.domain.QOrder;
import com.sparta.spartaeats.payments.domain.QPayment;
import com.sparta.spartaeats.payments.dto.PaymentResponseDto;
import com.sparta.spartaeats.payments.dto.PaymentSearchCond;
import com.sparta.spartaeats.payments.dto.QPaymentResponseDto;
import com.sparta.spartaeats.types.PaymentStatus;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.sparta.spartaeats.order.domain.QOrder.*;
import static com.sparta.spartaeats.payments.domain.QPayment.*;
import static org.springframework.util.StringUtils.hasText;

public class PaymentsRepositoryCustomImpl implements PaymentsRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public PaymentsRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<PaymentResponseDto> findPaymentList(Pageable pageable, PaymentSearchCond cond) {
        List<PaymentResponseDto> content = queryFactory
                .select(new QPaymentResponseDto(
                        payment.id,
                        payment.payment_amount.as("price"),
                        payment.paymentStatus.as("status")
                ))
                .from(payment)
                .where(
                        paymentStatusEq(cond.getStatus()),
                        createdByEq(cond.getCreatedBy()),
                        pgTypeEq(cond.getPgType()),
                        createdDateBetween(cond.getStartDate(), cond.getEndDate())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(payment.count())
                .from(payment)
                .where(
                        paymentStatusEq(cond.getStatus()),
                        createdByEq(cond.getCreatedBy()),
                        pgTypeEq(cond.getPgType())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression paymentStatusEq(PaymentStatus status) {
        return status != null ? payment.paymentStatus.eq(status) : null;
    }

    private BooleanExpression createdByEq(Long userId) {
        return userId != null ? payment.createdBy.eq(userId) : null;
    }

    private BooleanExpression pgTypeEq(String pgType) {
        return hasText(pgType) ? payment.pgType.eq(pgType) : null;
    }

    private BooleanExpression createdDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }
        return order.createdAt.between(startDate, endDate);
    }
}

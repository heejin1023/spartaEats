package com.sparta.spartaeats.payments.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;

import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.spartaeats.common.exception.EmptyDataException;
import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.order.domain.QOrder;
import com.sparta.spartaeats.order.dto.OrderListResponseDto;
import com.sparta.spartaeats.payments.domain.Payment;
import com.sparta.spartaeats.payments.domain.QPayment;
import com.sparta.spartaeats.payments.dto.PaymentResponseDto;
import com.sparta.spartaeats.payments.dto.PaymentSearchCond;
import com.sparta.spartaeats.payments.dto.QPaymentResponseDto;
import com.sparta.spartaeats.common.type.PaymentStatus;
import com.sparta.spartaeats.responseDto.MultiResponseDto;
import com.sparta.spartaeats.responseDto.PageInfoDto;
import com.sparta.spartaeats.store.domain.QStore;
import com.sparta.spartaeats.store.domain.Store;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.sparta.spartaeats.order.domain.QOrder.*;
import static com.sparta.spartaeats.payments.domain.QPayment.*;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
public class PaymentsRepositoryCustomImpl implements PaymentsRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public PaymentsRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public MultiResponseDto<PaymentResponseDto> findPaymentList(Pageable pageable, PaymentSearchCond cond) {
        /*List<PaymentResponseDto> content = queryFactory
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
                .fetch();*/
        int pageSize = pageable.getPageSize();
        if (pageSize == 10 || pageSize == 30 || pageSize == 50) {
            pageSize = 10;
        }
        List<OrderSpecifier<?>> paymentSpecifiers = new ArrayList<>();
        pageable.getSort().forEach(sort -> {
            OrderSpecifier<?> paymentSpecifier = null;
            if (sort.getProperty().equalsIgnoreCase("modifiedAt")) {
                paymentSpecifier = sort.isAscending() ? QPayment.payment.modifiedAt.asc() : QPayment.payment.modifiedAt.desc();
            } else if (sort.getProperty().equalsIgnoreCase("createdAt")) {
                paymentSpecifier = sort.isAscending() ? QPayment.payment.createdAt.asc() : QPayment.payment.createdAt.desc();
            }
            if (paymentSpecifier != null) {
                paymentSpecifiers.add(paymentSpecifier);
            }
        });
        List<Payment> list = queryFactory
                .selectFrom(payment)
                .from(payment)
                .where(
                        paymentStatusEq(cond.getStatus()),
                        createdByEq(cond.getCreatedBy()),
                        pgTypeEq(cond.getPgType()),
                        createdDateBetween(cond.getStartDate(), cond.getEndDate())
                )
                .orderBy(paymentSpecifiers.toArray(new OrderSpecifier<?>[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(payment.count())
                .from(payment)
                .where(
                        paymentStatusEq(cond.getStatus()),
                        createdByEq(cond.getCreatedBy()),
                        pgTypeEq(cond.getPgType()),
                        createdDateBetween(cond.getStartDate(), cond.getEndDate())
                );

        List<PaymentResponseDto> content = list.stream()
                .map(PaymentResponseDto::new).toList();
        Page<PaymentResponseDto> page = PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);

        if(page.isEmpty()){
            log.error("PaymentService.getAllPayments");
            throw new EmptyDataException("결제 내역이 존재하지 않습니다");
        }

        return new MultiResponseDto<>(ApiResultError.NO_ERROR,
                "Payment List 조회",
                page.getContent(),
                new PageInfoDto((int) page.getTotalElements(),
                        page.getSize(),
                        page.getNumber(),
                        page.getTotalPages(),
                        page.hasPrevious(),
                        page.hasNext()));
    }

    @Override
    public MultiResponseDto<PaymentResponseDto> findPaymentListWithUserRole(Pageable pageable, PaymentSearchCond cond, Long userId) {
        int pageSize = pageable.getPageSize();
        if (pageSize == 10 || pageSize == 30 || pageSize == 50) {
            pageSize = 10;
        }
        Pageable requestPage = PageRequest.of(pageable.getPageNumber(),pageSize,pageable.getSort());
        List<OrderSpecifier<?>> paymentSpecifiers = new ArrayList<>();
        pageable.getSort().forEach(sort -> {
            OrderSpecifier<?> paymentSpecifier = null;
            if (sort.getProperty().equalsIgnoreCase("modifiedAt")) {
                paymentSpecifier = sort.isAscending() ? QPayment.payment.modifiedAt.asc() : QPayment.payment.modifiedAt.desc();
            } else if (sort.getProperty().equalsIgnoreCase("createdAt")) {
                paymentSpecifier = sort.isAscending() ? QPayment.payment.createdAt.asc() : QPayment.payment.createdAt.desc();
            }
            if (paymentSpecifier != null) {
                paymentSpecifiers.add(paymentSpecifier);
            }
        });

        List<Payment> findPayment = queryFactory
                .selectFrom(payment)
                .leftJoin(payment.order, QOrder.order)
                .where(
                        paymentStatusEq(cond.getStatus()),
                        createdByEq(cond.getCreatedBy()),
                        pgTypeEq(cond.getPgType()),
                        createdDateBetween(cond.getStartDate(), cond.getEndDate()),
                        payment.order.user.id.eq(userId)
                )
                .orderBy(paymentSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(requestPage.getOffset())
                .limit(requestPage.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(payment.count())
                .from(payment)
                .leftJoin(payment.order, order)
                .where(
                        paymentStatusEq(cond.getStatus()),
                        createdByEq(cond.getCreatedBy()),
                        pgTypeEq(cond.getPgType()),
                        createdDateBetween(cond.getStartDate(), cond.getEndDate()),
                        payment.order.user.id.eq(userId)
                );
//        Page<PaymentResponseDto> page = PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);

        List<PaymentResponseDto> content = findPayment.stream()
                .map(PaymentResponseDto::new).toList();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), content.size());
        Page<PaymentResponseDto> page = new PageImpl<>(content.subList(start, end), pageable, content.size());
        if(page.isEmpty()){
            log.error("PaymentService.getAllPayments");
            throw new EmptyDataException("결제 내역이 존재하지 않습니다");
        }

        return new MultiResponseDto<>(ApiResultError.NO_ERROR,
                "Payment List 조회",
                page.getContent(),
                new PageInfoDto((int) page.getTotalElements(),
                        page.getSize(),
                        page.getNumber(),
                        page.getTotalPages(),
                        page.hasPrevious(),
                        page.hasNext()));
    }

    @Override
    public MultiResponseDto<PaymentResponseDto> findPaymentListWithOwnerRole(Pageable pageable, PaymentSearchCond cond, UUID storeId) {
        int pageSize = pageable.getPageSize();
        if (pageSize == 10 || pageSize == 30 || pageSize == 50) {
            pageSize = 10;
        }
        List<OrderSpecifier<?>> paymentSpecifiers = new ArrayList<>();
        pageable.getSort().forEach(sort -> {
            OrderSpecifier<?> paymentSpecifier = null;
            if (sort.getProperty().equalsIgnoreCase("modifiedAt")) {
                paymentSpecifier = sort.isAscending() ? QPayment.payment.modifiedAt.asc() : QPayment.payment.modifiedAt.desc();
            } else if (sort.getProperty().equalsIgnoreCase("createdAt")) {
                paymentSpecifier = sort.isAscending() ? QPayment.payment.createdAt.asc() : QPayment.payment.createdAt.desc();
            }
            if (paymentSpecifier != null) {
                paymentSpecifiers.add(paymentSpecifier);
            }
        });

        List<Payment> findPayment = queryFactory
                .selectFrom(payment)
                .leftJoin(payment.order, order)
                .leftJoin(order.store, QStore.store)
                .where(
                        paymentStatusEq(cond.getStatus()),
                        createdByEq(cond.getCreatedBy()),
                        pgTypeEq(cond.getPgType()),
                        createdDateBetween(cond.getStartDate(), cond.getEndDate()),
                        payment.order.store.id.eq(storeId)
                )
                .orderBy(paymentSpecifiers.toArray(new OrderSpecifier<?>[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(payment.count())
                .from(payment)
                .leftJoin(payment.order, order)
                .leftJoin(order.store, QStore.store)
                .where(
                        paymentStatusEq(cond.getStatus()),
                        createdByEq(cond.getCreatedBy()),
                        pgTypeEq(cond.getPgType()),
                        createdDateBetween(cond.getStartDate(), cond.getEndDate()),
                        payment.order.store.id.eq(storeId)
                );
        List<PaymentResponseDto> content = findPayment.stream()
                .map(PaymentResponseDto::new).toList();
        Page<PaymentResponseDto> page = PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);

        if(page.isEmpty()){
            log.error("PaymentService.getAllPayments");
            throw new EmptyDataException("결제 내역이 존재하지 않습니다");
        }

        return new MultiResponseDto<>(ApiResultError.NO_ERROR,
                "Payment List 조회",
                page.getContent(),
                new PageInfoDto((int) page.getTotalElements(),
                        page.getSize(),
                        page.getNumber(),
                        page.getTotalPages(),
                        page.hasPrevious(),
                        page.hasNext()));
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


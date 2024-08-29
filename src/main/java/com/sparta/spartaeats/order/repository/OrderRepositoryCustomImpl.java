package com.sparta.spartaeats.order.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.responseDto.MultiResponseDto;
import com.sparta.spartaeats.responseDto.PageInfoDto;
import com.sparta.spartaeats.entity.*;
import com.sparta.spartaeats.exception.EmptyDataException;
import com.sparta.spartaeats.order.dto.*;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.sparta.spartaeats.entity.QOrder.*;
import static com.sparta.spartaeats.entity.QOrderProduct.*;
import static com.sparta.spartaeats.entity.QStore.*;
import static com.sparta.spartaeats.entity.QUser.*;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public OrderRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public MultiResponseDto searchOrders(OrderSearchCondition cond, Pageable pageable) {
        List<Order> orders = queryFactory
                .select(order)
                .from(order)
                .leftJoin(order.store, store)
                .leftJoin(order.user, user)
                .fetchJoin()
                .where(
                        productNameEq(cond.getProductName()),
                        storeNameEq(cond.getStoreName()),
                        categoryEq(cond.getCategory()),
                        userNameEq(cond.getUsername()),
                        createdDateBetween(cond.getStartDate(), cond.getEndDate()),
                        order.delYn.eq('N')
                )
                .fetch();

        if (orders.isEmpty()) {
            log.error("orderRepositoryCustomImpl.searchOrders Error : List<Order> orders is empty");
            throw new EmptyDataException("데이터가 존재하지 않습니다");
        }

        //OrderProduct 컬렉션은 따로 가져오기
        List<OrderProduct> orderProducts = queryFactory
                .selectFrom(orderProduct)
                .join(orderProduct.order, order)
                .where(orderProduct.order.in(orders))
                .fetch();

        Map<UUID, List<OrderProductDto>> orderProductMap = orderProducts.stream()
                .collect(Collectors.groupingBy(
                        op -> op.getOrder().getId(),
                        Collectors.mapping(op -> new OrderProductDto(
                                op.getProduct().getId(),
                                op.getAmount(),
                                op.getPrice()
                        ), Collectors.toList())
                ));


        // OrderListResponseDto로 매핑
        List<OrderListResponseDto> content = orders.stream()
                .map(o -> new OrderListResponseDto(o, orderProductMap.getOrDefault(o.getId(), Collections.emptyList())))
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), content.size());
        Page<OrderListResponseDto> page = new PageImpl<>(content.subList(start, end), pageable, content.size());

        return new MultiResponseDto<>(
                ApiResultError.NO_ERROR.getCode(),
                "Order List 조회",
                page.getContent(),
                new PageInfoDto(
                        (int) page.getTotalElements(),
                        page.getSize(),
                        page.getNumber(),
                        page.getTotalPages(),
                        page.hasPrevious(),
                        page.hasNext()
                )
        );
    }

    private BooleanExpression productNameEq(String productName) {
        if (!hasText(productName)) {
            return null;
        }
        return JPAExpressions.selectFrom(orderProduct)
                .where(orderProduct.product.productName.eq(productName)
                        .and(orderProduct.order.eq(order)))
                .exists();
    }

    private BooleanExpression storeNameEq(String storeName) {
        return hasText(storeName) ? order.store.storeName.eq(storeName) : null;
    }

    private BooleanExpression categoryEq(String category) {
        return hasText(category) ? order.store.category.categoryName.eq(category) : null;
    }

    private BooleanExpression userNameEq(String userName) {
        return hasText(userName) ? order.user.userName.eq(userName) : null;
    }

    private BooleanExpression createdDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }
        return order.createdAt.between(startDate, endDate);
    }
}

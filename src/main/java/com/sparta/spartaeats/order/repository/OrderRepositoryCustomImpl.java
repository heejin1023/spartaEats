package com.sparta.spartaeats.order.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.order.domain.Order;
import com.sparta.spartaeats.order.domain.OrderProduct;
import com.sparta.spartaeats.order.domain.QOrder;
import com.sparta.spartaeats.responseDto.MultiResponseDto;
import com.sparta.spartaeats.responseDto.PageInfoDto;
import com.sparta.spartaeats.common.exception.EmptyDataException;
import com.sparta.spartaeats.order.dto.*;
import com.sparta.spartaeats.store.domain.Store;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.sparta.spartaeats.order.domain.QOrder.*;
import static com.sparta.spartaeats.order.domain.QOrderProduct.*;
import static com.sparta.spartaeats.store.domain.QStore.*;
import static com.sparta.spartaeats.user.domain.QUser.user;
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
    public MultiResponseDto searchOrdersWithUserRole(OrderSearchCondition cond, Pageable pageable, Long userId) {
        int pageSize = pageable.getPageSize();
        if (pageSize != 10 || pageSize != 30 || pageSize != 50) {
            pageSize = 10;
        }
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageSize, pageable.getSort());

        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        pageable.getSort().forEach(order -> {
            OrderSpecifier<?> orderSpecifier = null;
            if (order.getProperty().equalsIgnoreCase("modifiedAt")) {
                orderSpecifier = order.isAscending() ? QOrder.order.modifiedAt.asc() : QOrder.order.modifiedAt.desc();
            } else if (order.getProperty().equalsIgnoreCase("createdAt")) {
                orderSpecifier = order.isAscending() ? QOrder.order.createdAt.asc() : QOrder.order.createdAt.desc();
            }
            if (orderSpecifier != null) {
                orderSpecifiers.add(orderSpecifier);
            }
        });

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
                        order.delYn.eq('N'),
                        order.user.id.eq(userId)
                )
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier<?>[0]))
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
        Page<OrderListResponseDto> page = new PageImpl<>(content.subList(start, end), pageRequest, content.size());

        return new MultiResponseDto<>(
                ApiResultError.NO_ERROR,
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

    @Override
    public MultiResponseDto searchOrdersWithOwnerRole(OrderSearchCondition cond, Pageable pageable, Store findStore) {
        int pageSize = pageable.getPageSize();
        if (pageSize != 10 || pageSize != 30 || pageSize != 50) {
            pageSize = 10;
        }
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        pageable.getSort().forEach(order -> {
            OrderSpecifier<?> orderSpecifier = null;
            if (order.getProperty().equalsIgnoreCase("modifiedAt")) {
                orderSpecifier = order.isAscending() ? QOrder.order.modifiedAt.asc() : QOrder.order.modifiedAt.desc();
            } else if (order.getProperty().equalsIgnoreCase("createdAt")) {
                orderSpecifier = order.isAscending() ? QOrder.order.createdAt.asc() : QOrder.order.createdAt.desc();
            }
            if (orderSpecifier != null) {
                orderSpecifiers.add(orderSpecifier);
            }
        });

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
                        order.delYn.eq('N'),
                        order.store.eq(findStore)
                )
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier<?>[0]))
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
                ApiResultError.NO_ERROR,
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

    @Override
    public MultiResponseDto searchOrders(OrderSearchCondition cond, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        if (pageSize != 10 || pageSize != 30 || pageSize != 50) {
            pageSize = 10;
        }
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        pageable.getSort().forEach(order -> {
            OrderSpecifier<?> orderSpecifier = null;
            if (order.getProperty().equalsIgnoreCase("modifiedAt")) {
                orderSpecifier = order.isAscending() ? QOrder.order.modifiedAt.asc() : QOrder.order.modifiedAt.desc();
            } else if (order.getProperty().equalsIgnoreCase("createdAt")) {
                orderSpecifier = order.isAscending() ? QOrder.order.createdAt.asc() : QOrder.order.createdAt.desc();
            }
            if (orderSpecifier != null) {
                orderSpecifiers.add(orderSpecifier);
            }
        });
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
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier<?>[0]))
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
                ApiResultError.NO_ERROR,
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
        return hasText(category) ? order.store.storeCategory.categoryName.eq(category) : null;
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

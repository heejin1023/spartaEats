package com.sparta.spartaeats.order.repository;

import com.sparta.spartaeats.responseDto.MultiResponseDto;
import com.sparta.spartaeats.order.dto.OrderSearchCondition;
import com.sparta.spartaeats.store.Store;
import com.sparta.spartaeats.user.domain.User;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    MultiResponseDto searchOrders(OrderSearchCondition cond, Pageable pageable);

    MultiResponseDto searchOrdersWithUserRole(OrderSearchCondition cond, Pageable pageable, Long userId);

    MultiResponseDto searchOrdersWithOwnerRole(OrderSearchCondition cond, Pageable pageable, Store findStore);
}

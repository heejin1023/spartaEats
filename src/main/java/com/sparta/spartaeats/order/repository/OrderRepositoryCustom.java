package com.sparta.spartaeats.order.repository;

import com.sparta.spartaeats.responseDto.MultiResponseDto;
import com.sparta.spartaeats.order.dto.OrderSearchCondition;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    MultiResponseDto searchOrders(OrderSearchCondition cond, Pageable pageable);
}

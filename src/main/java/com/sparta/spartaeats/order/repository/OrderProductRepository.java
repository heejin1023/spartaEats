package com.sparta.spartaeats.order.repository;

import com.sparta.spartaeats.order.domain.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderProductRepository extends JpaRepository<OrderProduct, UUID> {
}

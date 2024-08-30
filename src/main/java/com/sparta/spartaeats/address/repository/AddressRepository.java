package com.sparta.spartaeats.address.repository;

import com.sparta.spartaeats.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Delivery, UUID> {

    // 역할에 따른 조건부 조회 쿼리 메서드
   @Query("SELECT a FROM Delivery a WHERE a.user.id = :userId AND a.local = :local AND a.order.id = :orderId AND a.useYn = :useYn")
    List<Delivery> findByUserIdAndLocalAndOrderIdAndUseYn(@Param("userId") Long userId, @Param("local") String local, @Param("orderId") Long orderId, @Param("useYn") Character useYn, Pageable pageable);
}
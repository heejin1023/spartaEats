package com.sparta.spartaeats.address.repository;

import com.sparta.spartaeats.address.core.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {

    // 역할에 따른 조건부 조회 쿼리 메서드
    List<Address> findByUserIdAndConditions(Long userIdx, String local, Long orderId, Character useYn, Pageable pageable);
}
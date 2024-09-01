package com.sparta.spartaeats.address.repository;

import com.sparta.spartaeats.address.domain.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AddressRepositoryCustom {
    Page<Address> findByUserIdAndLocalAndOrderIdAndUseYn(Long userIdx, String local, UUID orderId, Character useYn, Pageable pageable);
}

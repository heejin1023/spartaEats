package com.sparta.spartaeats.address.repository;

import com.sparta.spartaeats.address.domain.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AddressRepositoryCustom {
    Page<Address> findByUserIdAndLocalAndOrderIdAndUseYn(Long userIdx, String local, Long orderId, Character useYn, Pageable pageable);
}

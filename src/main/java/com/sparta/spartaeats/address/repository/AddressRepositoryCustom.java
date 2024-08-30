package com.sparta.spartaeats.address.repository;

import com.sparta.spartaeats.address.core.Address;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface AddressRepositoryCustom {
    List<Address> findByUserIdAndConditions(Long userIdx, String local, Long orderId, Character useYn, Pageable pageable);
}

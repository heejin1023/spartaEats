package com.sparta.spartaeats.address.repository;

import com.sparta.spartaeats.address.domain.Address;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface AddressRepositoryCustom {
    List<Address> findByUserIdAndLocalAndOrderIdAndUseYn(Long userIdx, String local, Long orderId, Character useYn, Pageable pageable);
}

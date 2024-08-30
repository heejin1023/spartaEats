package com.sparta.spartaeats.address.repository;

import com.sparta.spartaeats.entity.*;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface AddressRepositoryCustom {
    List<Delivery> findByUserIdAndConditions(Long userIdx, String local, Long orderId, Character useYn, Pageable pageable);
}

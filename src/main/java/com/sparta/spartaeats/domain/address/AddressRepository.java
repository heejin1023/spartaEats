package com.sparta.spartaeats.domain.address;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    // 추가적인 커스텀 쿼리가 필요하면 이곳에 작성할 수 있습니다.
}

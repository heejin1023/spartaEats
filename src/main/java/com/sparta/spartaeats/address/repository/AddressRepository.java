package com.sparta.spartaeats.address.repository;

import com.sparta.spartaeats.address.domain.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID>, AddressRepositoryCustom {

   @Query("select a from Address a where a.id = :addressId and (a.delYn = 'N' or a.delYn = 'n')")
   Optional<Address> findByIdWithDel(UUID addressId);
}
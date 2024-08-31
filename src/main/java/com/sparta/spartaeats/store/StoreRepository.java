package com.sparta.spartaeats.store;

import com.sparta.spartaeats.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID>, JpaSpecificationExecutor<Store> {
    Optional<Store> findByIdAndDelYn(UUID storeId, String delYn);

    @Query("select s from Store s where s.id = :storeId and (s.delYn = 'N' or s.delYn = 'n')")
    Optional<Store> findByIdWithDel(@Param("storeId") UUID storeId);

    Optional<Store> findByOwner(User user);
}

package com.sparta.spartaeats.store_category;

import com.sparta.spartaeats.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreCategoryRepository extends JpaRepository<StoreCategory, UUID> {
    Optional<StoreCategory> findById(UUID categoryId);
    Optional<StoreCategory> findByIdAndDelYn(UUID storeId, String delYn);
}

package com.sparta.spartaeats.storeCategory.repository;

import com.sparta.spartaeats.storeCategory.domain.StoreCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreCategoryRepository extends JpaRepository<StoreCategory, UUID>, JpaSpecificationExecutor<StoreCategory> {
    Optional<StoreCategory> findById(UUID categoryId);
    Optional<StoreCategory> findByIdAndDelYn(UUID storeId, Character delYn);

}

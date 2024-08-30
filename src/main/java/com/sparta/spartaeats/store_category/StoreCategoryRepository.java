package com.sparta.spartaeats.store_category;

import com.sparta.spartaeats.store.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreCategoryRepository extends JpaRepository<StoreCategory, UUID>, JpaSpecificationExecutor<StoreCategory> {
    Optional<StoreCategory> findById(UUID categoryId);
    Optional<StoreCategory> findByIdAndDelYn(UUID storeId, String delYn);

}

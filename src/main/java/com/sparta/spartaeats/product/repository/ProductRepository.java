package com.sparta.spartaeats.product.repository;

import com.sparta.spartaeats.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<com.sparta.spartaeats.entity.Product, UUID> {

    @Query("select p from Product p where p.id = :productId and p.delYn = 'N'")
    Optional<Product> findByIdWithDel(UUID productId);
}

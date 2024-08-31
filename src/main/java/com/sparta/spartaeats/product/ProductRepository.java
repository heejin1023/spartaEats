package com.sparta.spartaeats.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query("select p from Product p where p.id = :productId and (p.del_yn = 'N' or p.del_yn = 'n')")
    Optional<Product> findByIdWithDel(UUID productId);
}

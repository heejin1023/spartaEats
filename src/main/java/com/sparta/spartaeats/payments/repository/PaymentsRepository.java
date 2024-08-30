package com.sparta.spartaeats.payments.repository;

import com.sparta.spartaeats.payments.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PaymentsRepository extends JpaRepository<Payment, UUID>, PaymentsRepositoryCustom {

    @Query("select p from Payment p where p.id = :paymentId and (p.delYn = 'N' or p.delYn = 'n')")
    Optional<Payment> findByIdWithDel(@Param("paymentId") UUID paymentId);
}

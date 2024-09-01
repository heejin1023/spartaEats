package com.sparta.spartaeats.payments.repository;

import com.sparta.spartaeats.payments.domain.Payment;
import com.sparta.spartaeats.payments.dto.PaymentResponseDto;
import com.sparta.spartaeats.payments.dto.PaymentSearchCond;
import com.sparta.spartaeats.responseDto.MultiResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PaymentsRepository extends JpaRepository<Payment, UUID>, PaymentsRepositoryCustom {

    @Query("select p from Payment p where p.id = :paymentId and (p.delYn = 'N' or p.delYn = 'n')")
    Optional<Payment> findByIdWithDel(@Param("paymentId") UUID paymentId);

    @Query("select p from Payment p join Order o on p.order.id = o.id where p.id = :paymentId and (p.delYn = 'N' or p.delYn = 'n') and o.user.id = :userId")
    Optional<Payment> findByIdWithDelWithUser(@Param("paymentId") UUID paymentId, @Param("userId") Long userId);

}

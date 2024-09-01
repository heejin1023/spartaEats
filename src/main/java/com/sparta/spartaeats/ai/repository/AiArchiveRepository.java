package com.sparta.spartaeats.ai.repository;

import com.sparta.spartaeats.ai.domain.AiArchive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface AiArchiveRepository extends JpaRepository<AiArchive, UUID> {


    @Query("SELECT a FROM AiArchive a " +
            "LEFT JOIN a.product p " +
            //"LEFT JOIN a.user u " +
            "WHERE (:productName IS NULL OR p.productName LIKE %:productName%)")
           // "AND (:userName IS NULL OR u.name LIKE %:userName%)")
//            "AND (:startDate IS NULL OR a.dateField >= :startDate) " + // 날짜 필터링 추가
//            "AND (:endDate IS NULL OR a.dateField <= :endDate)")    // 날짜 필터링 추가
    Page<AiArchive> searchWithJoinAndLike(
            @Param("productName") String productName,
            //@Param("userName") String userName,
//            @Param("startDate") LocalDateTime startDate,
//            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);


}
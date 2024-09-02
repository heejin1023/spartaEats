package com.sparta.spartaeats.ai.repository;

import com.sparta.spartaeats.ai.domain.AiArchive;
import com.sparta.spartaeats.ai.dto.AiArchiveResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.UUID;

public interface AiArchiveRepository extends JpaRepository<AiArchive, UUID> {

    @Query("SELECT new com.sparta.spartaeats.ai.dto.AiArchiveResponseDto(" +
            "a.id, " +
            "a.requestContents, " +
            "a.responseContents, " +
            "a.delYn, " +
            "a.createdAt, " +
            "p.productName, " +
            "u.userName) " +
            "FROM AiArchive a " +
            "LEFT JOIN a.product p " +
            "LEFT JOIN User u ON a.createdBy = u.id " +
            "WHERE (:productName IS NULL OR p.productName LIKE %:productName%) " +
            "AND (:userName IS NULL OR u.userName LIKE %:userName%) " +
            "AND (CAST(a.createdAt AS DATE) >= COALESCE(:startDate, CAST('2000-01-01' AS DATE))) " +
            "AND (CAST(a.createdAt AS DATE) <= COALESCE(:endDate, CAST('9999-12-31' AS DATE)))")
    Page<AiArchiveResponseDto> searchWithJoinAndLike(
            @Param("productName") String productName,
            @Param("userName") String userName,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);



}
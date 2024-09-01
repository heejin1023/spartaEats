package com.sparta.spartaeats.apiLog.repository;

import com.sparta.spartaeats.apiLog.domain.ApiLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.UUID;

public interface ApiLogRepository extends JpaRepository<ApiLog, UUID> {

    @Query("SELECT a FROM ApiLog a " +
            "WHERE (CAST(a.createdAt AS DATE) >= COALESCE(:startDate, CAST('2000-01-01' AS DATE))) " +
            "AND (CAST(a.createdAt AS DATE) <= COALESCE(:endDate, CAST('9999-12-31' AS DATE)))")
    Page<ApiLog> getApiLogList(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
}


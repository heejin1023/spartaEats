package com.sparta.spartaeats.apiLog.service;

import com.sparta.spartaeats.apiLog.domain.ApiLog;
import com.sparta.spartaeats.apiLog.dto.ApiLogSearchCondition;
import com.sparta.spartaeats.apiLog.repository.ApiLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApiLogService {

    private final ApiLogRepository apiLogRepository;

    public ApiLog createLog(ApiLog apiLog) {
        return apiLogRepository.save(apiLog);
    }

    public Page<ApiLog> getApiLogList(ApiLogSearchCondition sc, Pageable pageable) {
        LocalDate startDate = sc.getStartDate();
        LocalDate endDate = sc.getEndDate();

        return apiLogRepository.getApiLogList(startDate, endDate, pageable);
    }

    public ApiLog getApiLogById(UUID apiId) {
        return apiLogRepository.findById(apiId).orElse(null);
    }


}

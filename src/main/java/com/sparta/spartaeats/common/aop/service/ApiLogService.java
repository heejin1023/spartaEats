package com.sparta.spartaeats.common.aop.service;

import com.sparta.spartaeats.board.BoardVO;
import com.sparta.spartaeats.common.aop.domain.ApiLogVO;
import com.sparta.spartaeats.common.aop.repository.ApiLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiLogService {

    private final ApiLogRepository apiLogRepository;

    // 로그 등록y
    public ApiLogVO createLog(ApiLogVO apiLogVO) {
        return apiLogRepository.save(apiLogVO);
    }

    // 목록 조회
//    public Page<BoardVO> getApiLogList(Pageable pageable) {
//        return apiLogRepository.getApiLogList(pageable);
//    }


}

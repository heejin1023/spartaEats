package com.sparta.spartaeats.common.aop.repository;

import com.sparta.spartaeats.board.BoardVO;
import com.sparta.spartaeats.common.aop.domain.ApiLogVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface ApiLogRepository extends JpaRepository<ApiLogVO, UUID> {

    //Page<BoardVO> getApiLogList(Pageable pageable);
}


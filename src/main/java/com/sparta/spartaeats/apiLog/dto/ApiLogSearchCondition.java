package com.sparta.spartaeats.apiLog.dto;

import com.sparta.spartaeats.common.dto.BaseSearchDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiLogSearchCondition extends BaseSearchDto {

    private LocalDate startDate = LocalDate.MIN;
    private LocalDate endDate = LocalDate.MAX;

    // 추가적인 생성자나 메서드가 필요할 수 있음
}
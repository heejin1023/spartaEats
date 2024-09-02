package com.sparta.spartaeats.ai.dto;

import com.sparta.spartaeats.common.dto.BaseSearchDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AiArchiveSearchCondition extends BaseSearchDto {

    private String userName;
    private String productName;
    private LocalDate startDate;
    private LocalDate endDate;
}

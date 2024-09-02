package com.sparta.spartaeats.ai.dto;

import com.sparta.spartaeats.common.dto.BaseSearchDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AiArchiveSearchCondition extends BaseSearchDto {

    private String userName;
    private String productName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}

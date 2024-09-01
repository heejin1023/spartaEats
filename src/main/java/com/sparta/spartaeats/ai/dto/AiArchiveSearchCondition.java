package com.sparta.spartaeats.ai.dto;

import com.sparta.spartaeats.common.dto.BaseSearchDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AiArchiveSearchCondition extends BaseSearchDto {

    private String userName;
    private String productName;
    private LocalDateTime startDate;

}

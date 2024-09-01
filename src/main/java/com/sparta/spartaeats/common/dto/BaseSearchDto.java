package com.sparta.spartaeats.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseSearchDto {

    private int pageSize;
    private int pageNumber;

    private String sort;
    private String direction;
}

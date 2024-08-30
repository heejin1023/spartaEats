package com.sparta.spartaeats.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageInfoDto {

    private int totalItem;
    private int pageItemSize;
    private int currentPage;
    private int totalPage;
    private boolean hasPrev;
    private boolean hasNext;
}

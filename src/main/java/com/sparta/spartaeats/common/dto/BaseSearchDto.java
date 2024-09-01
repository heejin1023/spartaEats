package com.sparta.spartaeats.common.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class BaseSearchDto {

    private Integer pageSize;
    private Integer pageNumber;

    private String sort;
    private String direction;

    public void validateAndSetDefaults() {
        if (pageSize == null || !(pageSize == 10 || pageSize == 30 || pageSize == 50)) {
            pageSize = 10;
        }

        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }

        if (direction == null) {
            direction = "asc";
        }

        if (sort == null) {
            sort = "createdAt";
        }
    }

    public Pageable generatePageable() {
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Sort sortOption = Sort.by(sortDirection, sort);
        return PageRequest.of(pageNumber - 1, pageSize, sortOption);
    }

}

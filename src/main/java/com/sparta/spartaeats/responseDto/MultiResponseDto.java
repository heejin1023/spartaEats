package com.sparta.spartaeats.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultiResponseDto<T> {
    private String resultCode;
    private String resultMessage;
    private List<T> resultData;
    private PageInfoDto pageInfo;
}

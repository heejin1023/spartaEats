package com.sparta.spartaeats.responseDto;

import com.sparta.spartaeats.common.model.ApiResult;
import com.sparta.spartaeats.common.type.ApiResultError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultiResponseDto<T> {
    private ApiResultError resultCode;
    private String resultMessage;
    private List<T> resultData;
    private PageInfoDto pageInfo;

    public MultiResponseDto(ApiResultError resultCode, String resultMessage) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }
}

package com.sparta.spartaeats.user.domain.dto;

import com.sparta.spartaeats.common.dto.BaseSearchDto;
import lombok.Data;

@Data
public class UserSearchCondition extends BaseSearchDto {

    private String userName;
    private String userContact;
    private String userEmail;

}

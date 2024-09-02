package com.sparta.spartaeats.user.domain.dto;

import com.sparta.spartaeats.common.dto.BaseSearchDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWarDeployment;

@Getter
@AllArgsConstructor
@ConditionalOnNotWarDeployment
public class UserSearchCondition extends BaseSearchDto {

    private String userName;
    private String userContact;
    private String userEmail;

}

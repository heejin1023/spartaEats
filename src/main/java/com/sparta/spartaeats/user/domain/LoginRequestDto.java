package com.sparta.spartaeats.user.domain;

import com.sparta.spartaeats.common.type.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

    private String userId;
    private String userName;
    private String password;
    private String userEmail;
    private String userType; // 1. 관리자 2. 가게주인 3. 고객

    private String userRole;

    public String getUserRole() {
        if(null == userRole){
            return "";
        }
        return this.userRole = UserRoleEnum.valueOf(this.userRole).name();
    }
}
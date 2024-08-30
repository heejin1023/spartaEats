package com.sparta.spartaeats.user.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {

    private String userId;
    private String userName;
    private String password;
    private String userEmail;
    private String userRole;

}
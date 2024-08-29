package com.sparta.spartaeats.login.domain;

import com.sparta.spartaeats.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserInfo {

    private Long userIdx;
    private String userId;
    private String password;
    private String userName;
    private String userContact;
    private String userEmail;
    private String userRole;

    private String delYn;
    private String useYn;
    private LocalDateTime join_date;

    @Builder(builderMethodName = "loginUserInfoBuilder", builderClassName = "loginUserInfoBuilder")
    public LoginUserInfo(User user) {
        Assert.notNull(user, "user must not be null");
        // TODO
    }

}

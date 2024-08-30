package com.sparta.spartaeats.user.controller;

import com.sparta.spartaeats.common.aop.ApiLogging;
import com.sparta.spartaeats.common.model.ApiResult;
import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.user.domain.LoginRequestDto;
import com.sparta.spartaeats.user.domain.User;
import com.sparta.spartaeats.user.repository.UserRepository;
import com.sparta.spartaeats.user.service.LoginService;
import com.sparta.spartaeats.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final LoginService loginService;

    // 회원 가입
    @ApiLogging
    @PostMapping("/signup")
    public ApiResult signup(@RequestBody LoginRequestDto loginRequestDto) {
        // validation

        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        User user = loginService.signup(loginRequestDto);


        return apiResult.set(ApiResultError.NO_ERROR).setResultData(user);
    }
}

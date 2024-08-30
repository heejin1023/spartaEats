package com.sparta.spartaeats.user.controller;

import com.sparta.spartaeats.common.aop.ApiLogging;
import com.sparta.spartaeats.common.jwt.JwtUtil;
import com.sparta.spartaeats.common.model.ApiResult;
import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.common.type.UserRoleEnum;
import com.sparta.spartaeats.user.domain.UserRequestDto;
import com.sparta.spartaeats.user.domain.User;
import com.sparta.spartaeats.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
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

    private final UserService loginService;
    private final JwtUtil jwtUtil;

    /**
     * 회원가입
     * @param loginRequestDto
     * @return
     */
    @ApiLogging
    @PostMapping("/signup")
    public ApiResult signup(@RequestBody UserRequestDto loginRequestDto) {
        // validation

        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        User user = loginService.signup(loginRequestDto);


        return apiResult.set(ApiResultError.NO_ERROR).setResultData(user);
    }

    /**
     * 로그인
     * @param res
     * @param loginRequestDto
     * @return
     */
    @ApiLogging
    @PostMapping("/login")
    public ApiResult login(HttpServletResponse res,
                           @RequestBody UserRequestDto loginRequestDto) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);

        User user = userService.login(loginRequestDto);

        // JWT 생성 및 쿠키에 저장 후 Response 객체에 추가
        String token = jwtUtil.createToken(user.getUserId(), UserRoleEnum.valueOf(user.getUserRole()));
        jwtUtil.addJwtToCookie(token, res);
        return apiResult.set(ApiResultError.NO_ERROR);
    }
}

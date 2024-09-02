package com.sparta.spartaeats.user.controller;

import com.sparta.spartaeats.common.aop.ApiLogging;
import com.sparta.spartaeats.common.controller.CustomApiController;
import com.sparta.spartaeats.common.exception.UserException;
import com.sparta.spartaeats.common.jwt.JwtUtil;
import com.sparta.spartaeats.common.model.ApiResult;
import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.common.type.UserRoleEnum;
import com.sparta.spartaeats.user.domain.User;
import com.sparta.spartaeats.user.domain.dto.UserRequestDto;
import com.sparta.spartaeats.user.domain.dto.UserSearchCondition;
import com.sparta.spartaeats.user.domain.validationGroup.ValidUser0001;
import com.sparta.spartaeats.user.domain.validationGroup.ValidUser0002;
import com.sparta.spartaeats.user.domain.validationGroup.ValidUser0003;
import com.sparta.spartaeats.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController extends CustomApiController {

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
    public ApiResult signup(@RequestBody @Validated(ValidUser0001.class) UserRequestDto loginRequestDto,
                            Errors errors) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        if (errors.hasErrors()) { //파라미터 바인딩 오류시 리턴
            return bindError(errors, apiResult);
        }

        User user = loginService.signup(loginRequestDto);
        log.info("Login User {}", user);

        if(user != null) {
            return apiResult.set(ApiResultError.NO_ERROR);
        }

        return apiResult;
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
                           @RequestBody @Validated({ValidUser0002.class}) UserRequestDto loginRequestDto,
                           Errors errors) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        if (errors.hasErrors()) { //파라미터 바인딩 오류시 리턴
            return bindError(errors, apiResult);
        }

        try {
            User user = userService.login(loginRequestDto);
            return apiResult.set(ApiResultError.NO_ERROR, "로그인 성공").setResultData(user);
        } catch (UserException e) {
            return apiResult.set(e.getCode());
        }

    }

    /**
     * 회원 탈퇴
     * @param userIdx
     * @return
     */
    @ApiLogging
    @Secured({UserRoleEnum.Authority.OWNER, UserRoleEnum.Authority.ADMIN, UserRoleEnum.Authority.USER})
    @PatchMapping("/withdraw")
    public ApiResult withdrawAccount(@RequestParam Long userIdx) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        if(userIdx == null || userIdx <= 0) {
            return apiResult.set(ApiResultError.NO_AUTH_NOT_FOUND_USER);
        }

        User loginUser = getLoginedUserObject();
        try {
            User user = userService.withdrawAccount(userIdx, loginUser);
            apiResult.set(ApiResultError.NO_ERROR, "탈퇴처리 되었습니다.");
        } catch(UserException e) {
            return apiResult.set(e.getCode());
        }

        return apiResult;
    }

    /**
     * 회원정보 수정
     * @param userRequestDto
     * @param errors
     * @return
     */
    @ApiLogging
    @Secured({UserRoleEnum.Authority.OWNER, UserRoleEnum.Authority.ADMIN, UserRoleEnum.Authority.USER})
    @PatchMapping
    public ApiResult upodateUserInfo(@RequestBody @Validated(ValidUser0003.class) UserRequestDto userRequestDto,
                     Errors errors) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        if (errors.hasErrors()) { //파라미터 바인딩 오류시 리턴
            return bindError(errors, apiResult);
        }
        User loginUser = getLoginedUserObject();
        try{
            User user = userService.updateUserInfo(userRequestDto, loginUser);

            return apiResult.set(ApiResultError.NO_ERROR).setResultMessage("수정되었습니다")
                    .setResultData(user);

        } catch (UserException e) {
            return apiResult.set(e.getCode());
        }

    }

    /**
     * 회원정보 상세조회
     * @param userIdx
     * @return
     */
    @ApiLogging
    @Secured({UserRoleEnum.Authority.OWNER, UserRoleEnum.Authority.ADMIN, UserRoleEnum.Authority.USER})
    @GetMapping("/{user_idx}")
    public ApiResult getUserInfo(@PathVariable("user_idx") Long userIdx) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        if(userIdx == null || userIdx <= 0) {
            return apiResult.set(ApiResultError.NO_AUTH_NOT_FOUND_USER);
        }

        User loginUser = getLoginedUserObject();

        try {
            User user = userService.getUserInfo(userIdx, loginUser);
            return apiResult.set(ApiResultError.NO_ERROR).setResultData(user);
        } catch (UserException e) {
            return apiResult.set(e.getCode());
        }

    }

    /**
     * 회원 목록 조회
     * @param sc
     * @return
     */
    @ApiLogging
    @Secured({UserRoleEnum.Authority.OWNER, UserRoleEnum.Authority.ADMIN})
    @GetMapping
    public ApiResult getUserList(UserSearchCondition sc) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);

        sc.validateAndSetDefaults();
        Pageable pageable = sc.generatePageable();

        User user = getLoginedUserObject();

        try {
            Page<User> userList = userService.getUserList(sc, pageable, user);
            apiResult.set(ApiResultError.NO_ERROR).setList(userList).setPageInfo(userList);
            return apiResult;
        } catch (UserException e) {
            return apiResult.set(e.getCode());
        }
    }
}

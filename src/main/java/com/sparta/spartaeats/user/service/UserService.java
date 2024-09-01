package com.sparta.spartaeats.user.service;


import com.sparta.spartaeats.common.exception.UserException;
import com.sparta.spartaeats.common.jwt.JwtUtil;
import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.common.type.UserRoleEnum;
import com.sparta.spartaeats.user.domain.User;
import com.sparta.spartaeats.user.domain.dto.UserRequestDto;
import com.sparta.spartaeats.user.domain.dto.UserSearchCondition;
import com.sparta.spartaeats.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public User signup(UserRequestDto loginUserInfo) {
        String userId = loginUserInfo.getUserId();
        String password = passwordEncoder.encode(loginUserInfo.getPassword());
        loginUserInfo.setPassword(password);
        // TODO: 회원 중복 확인
//        Optional<User> checkUsername = userRepository.findByUserId(userId);
//        if (checkUsername.isPresent()) {
//            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
//        }

        // email 중복확인
//        String email = requestDto.getEmail();
//        Optional<User> checkEmail = userRepository.findByEmail(email);
//        if (checkEmail.isPresent()) {
//            throw new IllegalArgumentException("중복된 Email 입니다.");
//        }
        // TODO 권한 확인 후 가입 방법이 달라야 하는지
        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;


        // 사용자 등록
        User user = User.SignUpUserInfoBuilder()
                .loginRequestDto(loginUserInfo)
                .build();
        return userRepository.save(user);
    }

    public User login(UserRequestDto loginUserInfo) throws UserException {
        String userId = loginUserInfo.getUserId();
        String password = loginUserInfo.getPassword();

        // 사용자 확인
        User user = userRepository.findByUserId(userId);
        if(null == user) {
            throw new UserException(ApiResultError.LOGIN_ERR_NOT_FOUND_ID);
        }

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserException(ApiResultError.LOGIN_ERR_NOT_MATCH_PASSWD);
        }

        // 활성화 여부 확인
        if(user.getUseYn() == 'N') {{
            throw new UserException(ApiResultError.LOGIN_ERR_NOT_USED_USER);
        }}

        return user;

    }

    public User getUserInfo(Long userIdx, User loginUser) throws UserException {
        // 사용자 확인
        User user = userRepository.findById(userIdx).orElseThrow(
                () -> new UserException(ApiResultError.LOGIN_ERR_NOT_FOUND_ID)
        );

        Long loginUserIdx = loginUser.getId();
        if(!userIdx.equals(loginUserIdx)) {
            //  로그인한 계정과 조회할 계정의 계정주가 다를 경우 관리자만 조회할 수 있게 처리
            if(loginUser.getUserRole().equals(UserRoleEnum.USER) || loginUser.getUserRole().equals(UserRoleEnum.OWNER)) {
                throw new UserException(ApiResultError.USER_INFO_ACCESS_DENIED);
            }
        }

        return user;
    }

    public User withdrawAccount(Long userIdx, User loginUser) throws UserException {
        // 사용자 확인
        User user = userRepository.findById(userIdx).orElseThrow(
                () -> new UserException(ApiResultError.LOGIN_ERR_NOT_FOUND_ID)
        );

        Long loginUserIdx = loginUser.getId();
        if(!userIdx.equals(loginUserIdx)) {
            //  로그인한 계정과 탈퇴할 계정주가 다를 경우 관리자면 탈퇴할 수 있도록 처리

            if(loginUser.getUserRole().equals(UserRoleEnum.USER) || loginUser.getUserRole().equals(UserRoleEnum.OWNER)) {
                throw new UserException(ApiResultError.USER_WITHDRAW_NO_AUTH);
            }
        }

        user.setUseYn('N');
        user.setDelYn('Y');
        user.setDeletedAt(LocalDateTime.now());
        user.setDeletedBy(userIdx);

        return userRepository.save(user);

    }

    public User updateUserInfo(UserRequestDto userRequestDto, User loginUser) throws UserException {
        Long userIdx = userRequestDto.getUserIdx();
        // 사용자 확인
        User user = userRepository.findById(userIdx).orElseThrow(
                () -> new UserException(ApiResultError.LOGIN_ERR_NOT_FOUND_ID)
        );

        // 권한 확인
        Long loginUserIdx = loginUser.getId();
        if(!userIdx.equals(loginUserIdx)) {
            if(!loginUser.getUserRole().equals(UserRoleEnum.ADMIN)) {
               throw new UserException(ApiResultError.USER_UPDATE_NO_AUTH);
            }
        }

        // 탈퇴한 회원이면 수정 불가
        if(user.getUseYn() == 'N' || user.getDelYn() == 'Y') {
            throw new UserException(ApiResultError.USER_UPDATE_NOT_ALLOWED);
        }

        String name = userRequestDto.getUserName() != null ?
                userRequestDto.getUserName() : user.getUserName();

        String password = userRequestDto.getPassword() != null ?
                passwordEncoder.encode(userRequestDto.getPassword()) : user.getPassword();

        String email = userRequestDto.getUserEmail() != null ?
                userRequestDto.getUserEmail() : user.getUserEmail();

        String contact = userRequestDto.getUserContact() != null ?
                userRequestDto.getUserContact() : user.getUserContact();

        user.setUserName(name);
        user.setPassword(password);
        user.setUserEmail(email);
        user.setUserContact(contact);
        user.setModifiedAt(LocalDateTime.now());
        user.setModifiedBy(loginUserIdx);

       return userRepository.save(user);
    }

    public Page<User> getUserList(UserSearchCondition sc, Pageable pageable, User loginUser) throws UserException {
        // 일반 고객의 경우 고객 목록 조회 불가
//        if(loginUser.getUserRole().equals(UserRoleEnum.USER)) {
//            throw new UserException(ApiResultError.USER_INFO_ACCESS_DENIED);
//        }

        String userName = sc.getUserName();
        String userContact = sc.getUserContact();
        String userEmail = sc.getUserEmail();

        return userRepository.searchUsers(userName, userContact, userEmail, pageable);

    }
}
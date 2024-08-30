package com.sparta.spartaeats.user.service;


import com.sparta.spartaeats.common.jwt.JwtUtil;
import com.sparta.spartaeats.common.type.UserRoleEnum;
import com.sparta.spartaeats.user.domain.UserRequestDto;
import com.sparta.spartaeats.user.domain.User;
import com.sparta.spartaeats.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public User login(UserRequestDto loginUserInfo) {
        String userId = loginUserInfo.getUserId();
        String password = loginUserInfo.getPassword();

        // 사용자 확인
        User user = userRepository.findByUserId(userId);

        log.info(user.toString());
        if(null == user) {

        }
        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        // 권한 확인

        return user;

    }

    public User getUserInfo(String userId) {
        return userRepository.findByUserId(userId);
    }
}
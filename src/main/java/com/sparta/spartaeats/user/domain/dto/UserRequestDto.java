package com.sparta.spartaeats.user.domain.dto;

import com.sparta.spartaeats.common.type.UserRoleEnum;
import com.sparta.spartaeats.user.domain.validationGroup.ValidUser0001;
import com.sparta.spartaeats.user.domain.validationGroup.ValidUser0002;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {

    @NotBlank(groups = {ValidUser0001.class, ValidUser0002.class},
            message = "아이디를 입력해 주세요.")
    @Pattern(regexp = "^[a-z0-9]{6,10}$",
            groups = {ValidUser0001.class},
            message = "아이디는 최소 6자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 작성해 주세요.")
    private String userId;

    @NotBlank(groups = {ValidUser0001.class},
            message = "이름을 입력해 주세요.")
    @Pattern(regexp = "^[a-z가-힣]{2,10}$",
            groups = {ValidUser0001.class},
            message = "이름은 최소 2자 이상, 10자 이하이며 영문 소문자(a~z) 또는 한글로 작성해 주세요.")
    private String userName;

    @NotBlank(groups = {ValidUser0001.class, ValidUser0002.class},
            message = "비밀번호를 입력해 주세요.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,15}$",
            groups = {ValidUser0001.class},
            message = "비밀번호는 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자를 포함하여 입력해 주세요.")
    private String password;


    @NotBlank(groups = {ValidUser0001.class},
            message = "이메일을 입력해 주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            groups = {ValidUser0001.class},
            message = "이메일 형식이 다릅니다.")
    private String userEmail;

    @NotBlank(message = "사용자타입을 선택해 주세요.",
            groups = {ValidUser0001.class})
    @Pattern(regexp = "^(USER|OWNER|ADMIN)$",
            groups = {ValidUser0001.class},
            message = "존재하지 않는 사용자타입입니다.")
    private UserRoleEnum userRole;

    @Pattern(regexp = "^010-?\\d{4}-?\\d{4}$",
            groups = {ValidUser0001.class},
            message = "휴대폰 번호는 '010-1234-5678' 또는 '01012345678' 형식으로 입력해 주세요.")
    private String userContact;


}
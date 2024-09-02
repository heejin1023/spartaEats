package com.sparta.spartaeats.user.domain;

import com.sparta.spartaeats.common.type.UserRoleEnum;
import com.sparta.spartaeats.common.util.TimeStamped;
import com.sparta.spartaeats.user.domain.dto.UserRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "p_user")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_idx")
    private Long id;

    private String userId;
    private String password;
    private String userName;
    private String userContact;
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column
    private UserRoleEnum userRole;

    //@OneToMany(mappedBy = "user")
    //private List<Delivery> deliveryList = new ArrayList<>();

    //@OneToMany(mappedBy = "user")
    //private List<Order> orderList = new ArrayList<>();

    private Character delYn;
    private Character useYn;
    private LocalDateTime joinDate;


    @Builder(builderClassName = "SignUpUserInfoBuilder", builderMethodName = "SignUpUserInfoBuilder")
    public User(UserRequestDto loginRequestDto) {
        this.userId = loginRequestDto.getUserId();
        this.password = loginRequestDto.getPassword();
        this.userName = loginRequestDto.getUserName();
        this.userEmail = loginRequestDto.getUserEmail();
        this.delYn = 'N';
        this.useYn = 'Y';
        this.userRole = UserRoleEnum.valueOf(loginRequestDto.getUserRole());
    }
}
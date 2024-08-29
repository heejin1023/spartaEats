package com.sparta.spartaeats.user.domain;

import com.sparta.spartaeats.common.type.UserRoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Getter
@Table(name = "p_user")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_idx")
    private Long id;

    private String userId;
    private String password;
    private String userName;
    private String userContact;
    private String userEmail;

    @Column(columnDefinition = "VARCHAR(255)")
    private String userRole;

    //@OneToMany(mappedBy = "user")
    //private List<Delivery> deliveryList = new ArrayList<>();

    //@OneToMany(mappedBy = "user")
    //private List<Order> orderList = new ArrayList<>();

    private String delYn;
    private String useYn;
    private LocalDateTime joinDate;


    @Builder(builderClassName = "SignUpUserInfoBuilder", builderMethodName = "SignUpUserInfoBuilder")
    public User(LoginRequestDto loginRequestDto) {
        this.userId = loginRequestDto.getUserId();
        this.password = loginRequestDto.getPassword();
        this.userName = loginRequestDto.getUserName();
        this.userEmail = loginRequestDto.getUserEmail();
        this.delYn = "N";
        this.useYn = "Y";
        this.userRole = loginRequestDto.getUserRole();
    }
}
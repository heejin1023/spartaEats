package com.sparta.spartaeats.user.domain;

import com.sparta.spartaeats.common.util.TimeStamped;
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
    public User(UserRequestDto loginRequestDto) {
        this.userId = loginRequestDto.getUserId();
        this.password = loginRequestDto.getPassword();
        this.userName = loginRequestDto.getUserName();
        this.userEmail = loginRequestDto.getUserEmail();
        this.delYn = "N";
        this.useYn = "Y";
        this.userRole = loginRequestDto.getUserRole();
    }
}
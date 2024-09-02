package com.sparta.spartaeats.user.domain;

import com.sparta.spartaeats.common.type.UserRoleEnum;
import com.sparta.spartaeats.common.util.TimeStamped;
import com.sparta.spartaeats.user.domain.dto.UserRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "p_user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_idx")
    private Long id;

    @Column
    private String userId;

    @Column
    private String password;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_contact")
    private String userContact;

    @Column(name = "user_email")
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column
    private UserRoleEnum userRole;

    //@OneToMany(mappedBy = "user")
    //private List<Delivery> deliveryList = new ArrayList<>();

    //@OneToMany(mappedBy = "user")
    //private List<Order> orderList = new ArrayList<>();

    @Column
    private Character delYn;

    @Column
    private Character useYn;

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
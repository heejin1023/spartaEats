package com.sparta.spartaeats.entity;

import com.sparta.spartaeats.common.type.UserRoleEnum;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "p_user")
public class User extends TimeStamped{

    @Id
    @GeneratedValue
    @Column(name = "user_idx")
    private Long id;
    private String userId;
    private String password;
    private String userName;
    private String userContact;
    private String userEmail;
    @Enumerated(EnumType.STRING)
    private UserRoleEnum userRole;

    @OneToMany(mappedBy = "user")
    private List<Delivery> deliveryList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Order> orderList = new ArrayList<>();

    private Character delYn;
    private Character useYn;
    private LocalDateTime join_date;
}

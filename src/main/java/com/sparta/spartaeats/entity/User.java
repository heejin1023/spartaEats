package com.sparta.spartaeats.entity;

import com.sparta.spartaeats.common.type.UserRoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "p_user")
@NoArgsConstructor
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

    public User(String userId, String password, String userName, String userContact, String userEmail, UserRoleEnum userRole, List<Delivery> deliveryList, List<Order> orderList, Character delYn, Character useYn, LocalDateTime join_date) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.userContact = userContact;
        this.userEmail = userEmail;
        this.userRole = userRole;
        this.deliveryList = deliveryList;
        this.orderList = orderList;
        this.delYn = delYn;
        this.useYn = useYn;
        this.join_date = join_date;
    }
}

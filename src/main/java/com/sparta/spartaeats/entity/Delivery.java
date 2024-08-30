package com.sparta.spartaeats.entity;

import com.sparta.spartaeats.order.domain.Order;
import com.sparta.spartaeats.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Table(name = "p_delivery")
@NoArgsConstructor
public class Delivery extends TimeStamped{

    @Id
    @GeneratedValue
    @Column(name = "delvr_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    private String zip;
    private String address;
    private String local;
    private String address2;
    private String contact;
    private Character delYn;
    private Character useYn;

    public Delivery(User user, Order order, String zip, String address, String local, String address2, String contact, Character delYn, Character useYn) {
        this.user = user;
        this.order = order;
        this.zip = zip;
        this.address = address;
        this.local = local;
        this.address2 = address2;
        this.contact = contact;
        this.delYn = delYn;
        this.useYn = useYn;
    }
}

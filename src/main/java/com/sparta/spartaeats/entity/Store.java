package com.sparta.spartaeats.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "p_store")
@NoArgsConstructor
public class Store extends TimeStamped{

    @Id
    @GeneratedValue
    @Column(name = "store_id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(mappedBy = "store")
    private List<Order> orderList = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Product> productList = new ArrayList<>();

    private String storeName;
    private String storeContact;
    private String storeAddress;
    private Character useYn;
    private Character delYn;

    public Store(User owner, Category category, Location location, List<Order> orderList, List<Product> productList, String storeName, String storeContact, String storeAddress, Character useYn, Character delYn) {
        this.owner = owner;
        this.category = category;
        this.location = location;
        this.orderList = orderList;
        this.productList = productList;
        this.storeName = storeName;
        this.storeContact = storeContact;
        this.storeAddress = storeAddress;
        this.useYn = useYn;
        this.delYn = delYn;
    }
}

package com.sparta.spartaeats.store.domain;

import com.sparta.spartaeats.common.util.TimeStamped;
import com.sparta.spartaeats.location.domain.Location;
import com.sparta.spartaeats.order.domain.Order;
import com.sparta.spartaeats.product.domain.Product;
import com.sparta.spartaeats.storeCategory.domain.StoreCategory;
import com.sparta.spartaeats.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_store")
public class Store extends TimeStamped {

    @Id
    @GeneratedValue
    @Column(name = "store_id", length = 200, nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private StoreCategory storeCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @OneToMany(mappedBy = "store")
    private List<Order> orderList = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Product> productList = new ArrayList<>();

    @Column(name = "store_name", length = 200, nullable = false)
    private String storeName;

    @Column(name = "store_contact", length = 100, nullable = false)
    private String storeContact;

    @Column(name = "store_address", length = 200, nullable = false)
    private String storeAddress;

    @ColumnDefault("'Y'")
    @Column(name = "use_yn", length = 1, nullable = false)
    private String useYn;

    @ColumnDefault("'N'")
    @Column(name = "del_yn", length = 1, nullable = false)
    private String delYn;

//    public void update(UUID store_id, StoreRequestDto storeRequestDto) {
//        this.id = store_id;
//        this.storeName = storeRequestDto.getStoreName();
//        this.storeContact = storeRequestDto.getStoreContact();
//        this.storeAddress = storeRequestDto.getStoreAddress();
//        this.useYn = storeRequestDto.getUseYn();
//        this.
//    }

//    public void delete() {
//        this.delYn = "Y";
//        this.deletedAt = LocalDateTime.now();
//    }
}

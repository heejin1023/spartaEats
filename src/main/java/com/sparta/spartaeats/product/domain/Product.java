package com.sparta.spartaeats.product.domain;

import com.sparta.spartaeats.common.util.TimeStamped;
import com.sparta.spartaeats.store.domain.Store;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.UUID;

@Entity
@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_products")
public class Product extends TimeStamped {

    @Id
    @GeneratedValue
    @Column(name = "product_id", length = 200, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "product_name", length = 200)
    private String productName;

    @Column(name = "price")
    private Integer price;

    @Column(name = "product_description", length = 1000)
    private String productDescription;

    @ColumnDefault("'Y'")
    @Column(name = "use_yn", length = 1)
    private String useYn;

    @ColumnDefault("'N'")
    @Column(name = "del_yn", length = 1, nullable = false)
    private String delYn;
}

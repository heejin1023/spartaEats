package com.sparta.spartaeats.storeCategory.domain;


import com.sparta.spartaeats.common.util.TimeStamped;
import com.sparta.spartaeats.store.domain.Store;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;
import java.util.UUID;


@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_store_category")
public class StoreCategory extends TimeStamped {

    @Id
    @GeneratedValue
    @Column(name = "category_id", length = 200, nullable = false)
    private UUID id;

    @Column(name = "category_name", length = 200, nullable = false)
    private String categoryName;

    @Column(name = "category_description", length = 200)
    private String categoryDescription;

    @Column(name = "use_yn", nullable = false, length = 1)
    private Character useYn;

    @Column(name = "del_yn", nullable = false, length = 1)
    private Character delYn;

    // 연관 관계: Store (1:N)
    @OneToMany(mappedBy = "storeCategory")
    private List<Store> stores;

    public StoreCategory(UUID id){
        this.id = id;
    }

}

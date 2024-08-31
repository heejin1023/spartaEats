package com.sparta.spartaeats.storeCategory;

import com.sparta.spartaeats.entity.TimeStamped;
import com.sparta.spartaeats.store.Store;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;
import java.util.UUID;


@Entity
@Data
@Builder
@Getter
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

    @ColumnDefault("'Y'")
    @Column(name = "use_yn", nullable = false, length = 1)
    private String useYn;

    @ColumnDefault("'N'")
    @Column(name = "del_yn", nullable = false, length = 1)
    private String delYn;

    // 연관 관계: Store (1:N)
    @OneToMany(mappedBy = "storeCategory")
    private List<Store> stores;

}

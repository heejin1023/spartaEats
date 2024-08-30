package com.sparta.spartaeats.ai;

import com.sparta.spartaeats.product.Product;
import com.sparta.spartaeats.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import java.util.UUID;

@Entity
@Data
@Builder
@Getter
@Table(name = "p_AI")
public class AI extends TimeStamped {
    
    @Id
    @GeneratedValue
    @Column(name = "ai_id", length = 200, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "request_contents", length = 1000)
    private String requestContents;

    @Column(name = "response_contents", length = 400)
    private String responseContents;

    @ColumnDefault("N")
    @Column(name = "del_yn", length = 1, nullable = false)
    private Character delYn;

}

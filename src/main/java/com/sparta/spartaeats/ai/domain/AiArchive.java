package com.sparta.spartaeats.ai.domain;

import com.sparta.spartaeats.common.util.TimeStamped;
import com.sparta.spartaeats.product.domain.Product;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "p_AI")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AiArchive extends TimeStamped {
    
    @Id
    @GeneratedValue
    @Column(name = "ai_id", length = 200, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;


    @Column(name = "request_contents", length = 1000)
    private String requestContents;

    @Column(name = "response_contents", length = 400)
    private String responseContents;

    @ColumnDefault("N")
    @Column(name = "del_yn", length = 1, nullable = false)
    private Character delYn;



}

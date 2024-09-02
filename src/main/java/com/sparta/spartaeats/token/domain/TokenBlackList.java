package com.sparta.spartaeats.token.domain;

import com.sparta.spartaeats.common.util.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "p_token_black_list")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenBlackList extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID tokenId;

    private Long userIdx;

    @Column(length = 1024)
    private String token;

    private Long blackListTime;

}
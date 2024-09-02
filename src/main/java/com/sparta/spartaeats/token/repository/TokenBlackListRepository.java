package com.sparta.spartaeats.token.repository;

import com.sparta.spartaeats.token.domain.TokenBlackList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenBlackListRepository extends JpaRepository<TokenBlackList, Long> {

    TokenBlackList findByToken(String token);
}

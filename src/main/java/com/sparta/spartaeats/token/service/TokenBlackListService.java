package com.sparta.spartaeats.token.service;

import com.sparta.spartaeats.token.domain.TokenBlackList;
import com.sparta.spartaeats.token.repository.TokenBlackListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class TokenBlackListService {

    private final TokenBlackListRepository tokenBlackListRepository;

    public TokenBlackList createTokenBlackList(Long userIdx, String token) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
        long timestampMillis = instant.toEpochMilli();

        TokenBlackList tokenBlackList = TokenBlackList.builder()
                .userIdx(userIdx)
                .token(token)
                .blackListTime(timestampMillis)
                .build();

        return tokenBlackListRepository.save(tokenBlackList);
    }

    public TokenBlackList getTokenBlackList(String token) {
        return tokenBlackListRepository.findByToken(token);
    }
}

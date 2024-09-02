package com.sparta.spartaeats.common.jwt;

import com.sparta.spartaeats.common.security.UserDetailsServiceImpl;
import com.sparta.spartaeats.token.domain.TokenBlackList;
import com.sparta.spartaeats.token.service.TokenBlackListService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final TokenBlackListService tokenBlackListService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, TokenBlackListService tokenBlackListService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.tokenBlackListService = tokenBlackListService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String tokenValue = jwtUtil.getJwtFromHeader(req);

        if (StringUtils.hasText(tokenValue)) {
            try {
                // 토큰 검증
                if(!jwtUtil.validateToken(tokenValue)) {
                    log.error("Token Error");
                    setErrorResponse(res, HttpServletResponse.SC_UNAUTHORIZED, "토큰이 만료되었거나 유효하지 않습니다.");
                    return;
                }

                TokenBlackList tokenBlackList = tokenBlackListService.getTokenBlackList(tokenValue);
                if(tokenBlackList != null) {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    log.error("Token Blacklist Error");
                    setErrorResponse(res, HttpServletResponse.SC_UNAUTHORIZED, "만료된 토큰입니다.");
                    return;
                }

                Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }

        }

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    // 응답에 에러 메시지 설정
    private void setErrorResponse(HttpServletResponse res, int statusCode, String message) throws IOException {
        res.setStatus(statusCode);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        // JSON 형식으로 에러 메시지 작성
        String jsonResponse = String.format("{\"error\": \"%s\"}", message);
        res.getWriter().write(jsonResponse);
    }
}
package com.StreetNo5.StreetNo5.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = resolveToken((HttpServletRequest) request);

        // 토큰 유효성 검사
        if (token!=null && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
/*        else if (token!=null){
            // 리프레시 토큰 검증 && 리프레시 토큰 DB에서  토큰 존재유무 확인
            boolean isRefreshToken = .refreshTokenValidation(refreshToken);
            // 리프레시 토큰이 유효하고 리프레시 토큰이 DB와 비교했을때 똑같다면
            if (isRefreshToken) {
                // 리프레시 토큰으로 아이디 정보 가져오기
                String loginId = jwtUtil.getEmailFromToken(refreshToken);
                // 새로운 어세스 토큰 발급
                String newAccessToken = jwtUtil.createToken(loginId, "Access");
                // 헤더에 어세스 토큰 추가
                jwtUtil.setHeaderAccessToken(response, newAccessToken);
                // Security context에 인증 정보 넣기
                setAuthentication(jwtUtil.getEmailFromToken(newAccessToken));
            }
            // 리프레시 토큰이 만료 || 리프레시 토큰이 DB와 비교했을때 똑같지 않다면
            else {
                jwtExceptionHandler(response, "RefreshToken Expired", HttpStatus.BAD_REQUEST);
                return;

        }*/
        chain.doFilter(request, response);
    }

    // 헤더에서 토큰 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

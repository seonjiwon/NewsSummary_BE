package com.seonjiwon.NewsSummarySpring.auth.jwtutil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT 인증 필터
 * 모든 요청마다 JWT 토큰을 검사하고, 유효한 경우 Spring Security 인증 객체를 등록
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;


    /**
     * HTTP 요청이 들어올 때마다 실행되는 필터
     * - JWT 토큰을 Authorization 헤더에서 추출
     * - 토큰이 유효하면 인증 객체(SecurityContext)에 등록
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 요청 헤더에서 JWT 토큰 추출
        String token = resolveToken(request);

        // 토큰이 존재하고 유효하면
        if (token != null && jwtTokenProvider.validation(token)) {
            // 토큰에서 사용자 식별 정보(email) 추출
            String email = jwtTokenProvider.getEmailFromToken(token);

            // 인증 객체 생성 (권한은 우선 empty)
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,  // principal (주체)
                            null, // credentials (자격 증명)
                            Collections.emptyList()); // authorities (권한 목록)

            // Spring Security의 SecurityContext에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Authorization 헤더에서 Bearer 토큰을 추출
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // "Bearer"로 시작하면 그 이후 문자열이 JWT
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후 부분만 반환
        }
        return null;
    }

}

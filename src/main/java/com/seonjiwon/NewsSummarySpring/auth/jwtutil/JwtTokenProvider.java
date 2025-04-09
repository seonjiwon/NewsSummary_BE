package com.seonjiwon.NewsSummarySpring.auth.jwtutil;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT 생성, 추출, 검증 등 JWT 관련 기능 담당 클래스
 */
@Component
public class JwtTokenProvider {
    // JWT 서명에 사용할 비밀 키
    private final SecretKey SECRET_KEY;
    //토큰 유효 시간
    private final Long EXPIRATION;

    @Autowired
    public JwtTokenProvider(Environment env) {
        this.EXPIRATION = Long.parseLong(env.getProperty("token.expiration_time")) ;

        String secret = env.getProperty("token.secret");
        
        // Base64로 인코딩
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        // HMAC-SHA 알고리즘을 통해 SecretKey 생성
        this.SECRET_KEY = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email, boolean isAdmin) {
        Date now = new Date();



        return Jwts.builder()
                .claim("email", email)
                .claim("isAdmin", isAdmin)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + EXPIRATION))
                .signWith(SECRET_KEY, Jwts.SIG.HS256)
                .compact();
    }

    // 토큰에서 사용자 email 추출
    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY) // 서명 검증
                .build() //parser 빌드
                .parseSignedClaims(token) // 서명된 클레임 생성
                .getPayload() // 클레임(payload) 꺼냄
                .get("email", String.class); // "email" 클레임 추출
    }

    public boolean getIsAdminFromToken(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("isAdmin", Boolean.class);
    }

    // 토큰 유효성 검증
    public boolean validation(String token) {
        try {
            Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (io.jsonwebtoken.security.SignatureException e) {
            System.out.println("❌ 서명 검증 실패: " + e.getMessage());
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.out.println("❌ 토큰 만료됨: " + e.getMessage());
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            System.out.println("❌ 잘못된 JWT 형식: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ 기타 토큰 오류: " + e.getMessage());
        }
        return false;
    }
}

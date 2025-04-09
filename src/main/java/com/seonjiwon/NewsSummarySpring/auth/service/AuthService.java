package com.seonjiwon.NewsSummarySpring.auth.service;

import com.seonjiwon.NewsSummarySpring.auth.dto.LoginResult;
import com.seonjiwon.NewsSummarySpring.auth.jwtutil.JwtTokenProvider;
import com.seonjiwon.NewsSummarySpring.user.User;
import com.seonjiwon.NewsSummarySpring.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public LoginResult login(String email, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("사용자 없음"));

            String token = jwtTokenProvider.generateToken(user.getEmail(), user.isAdmin());

            return new LoginResult(user.getId(), user.isAdmin(), token);

        } catch (Exception e) {
            System.out.println("❌ 로그인 실패: " + e.getMessage());
            throw e; // 다시 던져서 컨트롤러에서 처리하게 해도 OK
        }
    }
}

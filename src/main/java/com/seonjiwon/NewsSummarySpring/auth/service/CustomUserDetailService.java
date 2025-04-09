package com.seonjiwon.NewsSummarySpring.auth.service;

import com.seonjiwon.NewsSummarySpring.auth.dto.CustomUserDetails;
import com.seonjiwon.NewsSummarySpring.user.User;
import com.seonjiwon.NewsSummarySpring.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 이메일로 DB에서 사용자를 찾아서 CustomUserDetails로 감싸줌
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);

        return new CustomUserDetails(user.get());
    }
}

package com.seonjiwon.NewsSummarySpring.user;

import com.seonjiwon.NewsSummarySpring.user.dto.ChangeKeyword;
import com.seonjiwon.NewsSummarySpring.user.dto.SignupRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));
    }

    @Transactional
    public Long signUp(SignupRequest request) {
        log.info("loginService");
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }

        User user = new User(
                null,
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()), // 비밀번호 암호화
                request.getNickname(),
                request.getPreferredTopic(),
                request.getKeywords(),
                false, // 기본 회원가입은 일반 사용자
                null,
                null
        );

        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    @Transactional
    public void changeTopic(Long userId, ChangeKeyword changeKeyword){
        User user = findById(userId);

        user.changePreferredTopic(changeKeyword.getTopic());

        user.getKeywords().clear();
        user.getKeywords().addAll(changeKeyword.getKeywords());
    }

}

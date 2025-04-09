package com.seonjiwon.NewsSummarySpring.auth.controller;

import com.seonjiwon.NewsSummarySpring.auth.dto.LoginResult;
import com.seonjiwon.NewsSummarySpring.auth.dto.LoginUser;
import com.seonjiwon.NewsSummarySpring.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> userLogin(@RequestBody LoginUser loginUser){
      log.info("Email: {}, Password: {}", loginUser.getEmail(), loginUser.getPassword());

        try {
            LoginResult result = authService.login(loginUser.getEmail(), loginUser.getPassword());

            Map<String, Object> response = new HashMap<>();
            response.put("userId", result.getUserId());
            response.put("isAdmin", result.isAdmin());
            response.put("token", result.getToken());

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "잘못된 이메일 혹은 비밀번호 입니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
}

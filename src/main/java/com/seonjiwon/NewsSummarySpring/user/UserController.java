package com.seonjiwon.NewsSummarySpring.user;

import com.seonjiwon.NewsSummarySpring.user.dto.ChangeKeyword;
import com.seonjiwon.NewsSummarySpring.user.dto.SignupRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody SignupRequest request) {
        try {
            Long userId = userService.signUp(request);
            Map<String, Object> response = new HashMap<>();
            response.put("userId", userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/{userId}/keywords")
    public ResponseEntity<Map<String, Object>> getTopic(@PathVariable Long userId) {
        User user = userService.findById(userId);
        log.info("user: {}", user);
        log.info("userId: {}, topic: {}, keywords: {}", userId, user.getPreferredTopic(), user.getKeywords());
        Map<String, Object> response = new HashMap<>();
        response.put("topic", user.getPreferredTopic());
        response.put("keywords", user.getKeywords());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{userId}/keywords")
    public ResponseEntity<?> changeKeywords(@PathVariable Long userId,
                                            @RequestBody ChangeKeyword changeKeyword) {
        log.info("Topic: {}", changeKeyword.getTopic());
        userService.changeTopic(userId, changeKeyword);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}

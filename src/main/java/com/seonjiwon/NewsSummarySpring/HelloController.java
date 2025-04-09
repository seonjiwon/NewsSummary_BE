package com.seonjiwon.NewsSummarySpring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class HelloController {
    @GetMapping("/hello")
    public Map<String, String> helloMessage() {
        return Map.of("message", "프론트와 성공적으로 연결되었습니다.");
    }
}

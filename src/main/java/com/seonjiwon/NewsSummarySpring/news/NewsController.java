package com.seonjiwon.NewsSummarySpring.news;

import com.seonjiwon.NewsSummarySpring.common.GlobalCORS;
import com.seonjiwon.NewsSummarySpring.news.dto.NewsItem;
import com.seonjiwon.NewsSummarySpring.user.User;
import com.seonjiwon.NewsSummarySpring.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@GlobalCORS
public class NewsController {
    private final NaverNewsService naverNewsService;
    private final UserRepository userRepository;
    private final OpenAiService openAiService;

    @GetMapping("/{userId}/news")
    public ResponseEntity<Map<String, List<NewsItem>>> getNews(@PathVariable Long userId,
                                                              @RequestHeader("Authorization") String authHeader,
                                                              @RequestParam(defaultValue = "1") int page) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        return ResponseEntity.status(HttpStatus.OK).body(naverNewsService.getNews(user.getPreferredTopic(),
                user.getKeywords(),
                page));
    }

    @GetMapping("/{userId}/news-summary")
    public ResponseEntity<String> getSummaryNews(@PathVariable Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        List<NewsItem> newsList = naverNewsService.getPreferredNews(user.getPreferredTopic(), user.getKeywords());
        StringBuilder combinedText = naverNewsService.combinedNews(newsList);

        String summary = openAiService.summarize(combinedText.toString());

        return ResponseEntity.status(HttpStatus.OK).body(summary);

    }

}
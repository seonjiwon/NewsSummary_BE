package com.seonjiwon.NewsSummarySpring.news;

import com.seonjiwon.NewsSummarySpring.news.dto.ChatGPTRequest;
import com.seonjiwon.NewsSummarySpring.news.dto.ChatGPTResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    @Value("${openai.api-key}")
    private String apiKey;

    private final WebClient.Builder webClientBuilder;


    public String summarize(String text){
        String prompt = "다음은 사용자가 선호하는 분야의 뉴스 20개의 제목과 내용입니다. 감정 없이 중립적으로 핵심 내용을 간결하게 요약해 주세요: \n" + text;

        ChatGPTRequest request = new ChatGPTRequest(
                "gpt-4o",
                List.of(new ChatGPTRequest.Message("user", prompt))
        );

        ChatGPTResponse response = webClientBuilder.build().post()
                .uri("https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatGPTResponse.class)
                .block();

        return response.getChoices().get(0).getMessage().getContent();
    }
}

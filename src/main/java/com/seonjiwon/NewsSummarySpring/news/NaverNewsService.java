package com.seonjiwon.NewsSummarySpring.news;

import com.seonjiwon.NewsSummarySpring.news.dto.NaverNewsResponse;
import com.seonjiwon.NewsSummarySpring.news.dto.NewsItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NaverNewsService {

    @Value("${naver.client_id}")
    private String clientId;

    @Value("${naver.client_secret}")
    private String clientSecret;

    private final WebClient.Builder webClientBuilder;

    public List<NewsItem> searchNews(String query, int start, int size) {
        WebClient webClient = webClientBuilder.build();

        NaverNewsResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("openapi.naver.com")
                        .path("/v1/search/news.json")
                        .queryParam("query", query) // 검색어
                        .queryParam("start", start)
                        .queryParam("display", size) //한번에 표시할 결과 갯수
                        .queryParam("sort", "date") // date: 날짜순으로 내림차순 정렬, sim: 정확도순으로 내림차순 정렬(default)
                        .build()
                )
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .retrieve()
                .bodyToMono(NaverNewsResponse.class)
                .block();

        return response.getItems();
    }

    public Map<String, List<NewsItem>> getNews(String topic, List<String> keywords, int page) {
        int start = (page - 1) * 10 + 1;
        String CombinedQuery = buildQuery(topic, keywords);

        List<NewsItem> popularNews = cleanHtmlMarkup(searchNews("헤드라인", start, 10));
        List<NewsItem> topicNews = cleanHtmlMarkup(searchNews(CombinedQuery, start, 10));


        HashMap<String, List<NewsItem>> result = new HashMap<>();
        result.put("popular", popularNews);
        result.put("topic", topicNews);

        return result;
    }

    public List<NewsItem> cleanHtmlMarkup(List<NewsItem> newsItems){
        List<NewsItem> cleanedList = new ArrayList<>();

        for (NewsItem newsItem : newsItems) {
            String cleanTitle = Jsoup.parse(newsItem.getTitle()).text();
            String cleanDescription = Jsoup.parse(newsItem.getDescription()).text();

            NewsItem cleanedItem = new NewsItem(
                    cleanTitle,
                    newsItem.getLink(),
                    cleanDescription,
                    newsItem.getPubDate()
            );
            cleanedList.add(cleanedItem);
        }
        return cleanedList;
    }

    public List<NewsItem> getPreferredNews(String topic, List<String> keywords){
        String CombinedQuery = buildQuery(topic, keywords);
        return searchNews(CombinedQuery, 1, 20);

    }

    public StringBuilder combinedNews(List<NewsItem> newsList) {
        // 뉴스들을 하나의 텍스트로 합치기
        StringBuilder combinedText = new StringBuilder();
        for (int i = 0; i < newsList.size(); i++) {
            NewsItem news = newsList.get(i);
            combinedText.append(i + 1).append(". ").append(news.getTitle()).append("\n")
                    .append(news.getDescription()).append("\n\n");
        }
        log.info("{}", combinedText);

        return combinedText;
    }

    private String buildQuery(String topic, List<String> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return topic;
        }

        return topic + " " + String.join(" ", keywords);
    }
}

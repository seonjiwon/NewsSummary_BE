package com.seonjiwon.NewsSummarySpring.news.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class NewsItem {
    private String title;
    private String link;
    private String description;
    private String pubDate;
}

package com.seonjiwon.NewsSummarySpring.news.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class NaverNewsResponse {
    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<NewsItem> items;
}

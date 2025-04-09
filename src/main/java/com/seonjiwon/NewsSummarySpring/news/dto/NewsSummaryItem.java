package com.seonjiwon.NewsSummarySpring.news.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class NewsSummaryItem {
    private String title;
    private String original;
    private String summary;
}

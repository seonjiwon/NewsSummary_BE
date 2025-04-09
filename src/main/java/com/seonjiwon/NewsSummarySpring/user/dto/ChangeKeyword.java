package com.seonjiwon.NewsSummarySpring.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChangeKeyword {
    private String topic;         // 여기에 맞춰줘야 프론트와 맞음
    private List<String> keywords;
}

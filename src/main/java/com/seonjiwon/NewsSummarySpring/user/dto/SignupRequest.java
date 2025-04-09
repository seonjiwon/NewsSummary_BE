package com.seonjiwon.NewsSummarySpring.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class SignupRequest {
    private String email;
    private String password;
    private String nickname;
    private String preferredTopic;
    private List<String> keywords;
}

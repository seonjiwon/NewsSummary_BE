package com.seonjiwon.NewsSummarySpring.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResult {

    private Long userId;
    private boolean isAdmin;
    private String token;
}

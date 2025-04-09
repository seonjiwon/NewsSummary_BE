package com.seonjiwon.NewsSummarySpring.auth.dto;

import lombok.Data;

@Data
public class LoginUser {
    private String email;
    private String password;
}

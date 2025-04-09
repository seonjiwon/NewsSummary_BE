package com.seonjiwon.NewsSummarySpring.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해 허용
                .allowedOrigins("http://localhost:5173", "https://news-summary-fe.vercel.app") // 프론트 도메인 (Cursor라면 여기에 해당 주소)
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true); // 프론트에서 쿠키/헤더 인증 보내는 경우 꼭 필요
    }

}

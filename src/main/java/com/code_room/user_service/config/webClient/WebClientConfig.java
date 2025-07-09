package com.code_room.user_service.config.webClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${openai.api.key}")
    private String openApiKey;

    @Value("${openai.api.url}")
    private String openApiUrl;

    @Bean
    public WebClient openAIWebClient() {
        return WebClient.builder()
                .baseUrl(openApiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openApiKey)
                .build();
    }
}
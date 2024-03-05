package org.example.task.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    private static final String GITHUB_BASE_URL = "https://api.github.com";

    @Bean
    public WebClient gitHubClient() {
        return WebClient.builder()
                .baseUrl(GITHUB_BASE_URL)
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}

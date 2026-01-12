package com.cinemaabyss.proxy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Value("${monolith.url}")
    private String monolithUrl;

    @Value("${movies.service.url}")
    private String moviesServiceUrl;

    @Value("${events.service.url}")
    private String eventsServiceUrl;

    public String getMonolithUrl() {
        return monolithUrl;
    }

    public String getMoviesServiceUrl() {
        return moviesServiceUrl;
    }

    public String getEventsServiceUrl() {
        return eventsServiceUrl;
    }
}

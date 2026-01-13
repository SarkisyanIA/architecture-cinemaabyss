package com.cinemaabyss.proxy.service.impl;

import com.cinemaabyss.proxy.config.AppConfig;
import com.cinemaabyss.proxy.dto.ApiResponse;
import com.cinemaabyss.proxy.service.FeatureFlagService;
import com.cinemaabyss.proxy.service.ProxyService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Service
public class ProxyServiceImpl implements ProxyService {

    private final RestTemplate restTemplate;
    private final AppConfig appConfig;
    private final FeatureFlagService featureFlagService;

    public ProxyServiceImpl(RestTemplate restTemplate, AppConfig appConfig,
                            FeatureFlagService featureFlagService) {
        this.restTemplate = restTemplate;
        this.appConfig = appConfig;
        this.featureFlagService = featureFlagService;
    }

    @Override
    public ResponseEntity<ApiResponse> proxyRequest(String path, String method,
                                                    String requestBody, String headers) {
        // Определяем к какому сервису относится запрос
        if (path.startsWith("/api/movies")) {
            return proxyMoviesRequest(path, method, requestBody, headers);
        } else if (path.startsWith("/api/events")) {
            return proxyEventsRequest(path, method, requestBody, headers);
        }

        // Для других путей используем монолит
        return forwardToMonolith(path, method, requestBody, headers);
    }

    @Override
    public ResponseEntity<ApiResponse> proxyMoviesRequest(String path, String method,
                                                          String requestBody, String headers) {
        boolean useNewService = featureFlagService.shouldRouteToNewService("movies");

        String targetUrl;
        String serviceName;

        if (useNewService) {
            targetUrl = appConfig.getMoviesServiceUrl() + path;
            serviceName = "movies-service";
        } else {
            targetUrl = appConfig.getMonolithUrl() + path;
            serviceName = "monolith";
        }

        try {
            ResponseEntity<String> response = makeRequest(targetUrl, method, requestBody, headers);
            return ResponseEntity
                    .status(response.getStatusCode())
                    .body(new ApiResponse(true, response.getBody(),
                            "Success from " + serviceName, null,"success"));
        } catch (Exception e) {
            // Fallback к монолиту в случае ошибки
            if (useNewService) {
                return forwardToMonolith(path, method, requestBody, headers);
            }
            throw e;
        }
    }

    @Override
    public ResponseEntity<ApiResponse> proxyEventsRequest(String path, String method,
                                                          String requestBody, String headers) {
        // Пока всегда направляем в монолит, но можно добавить логику миграции позже
        return forwardToMonolith(path, method, requestBody, headers);
    }

    private ResponseEntity<ApiResponse> forwardToMonolith(String path, String method,
                                                          String requestBody, String headers) {
        String targetUrl = appConfig.getMonolithUrl() + path;

        try {
            ResponseEntity<String> response = makeRequest(targetUrl, method, requestBody, headers);
            return ResponseEntity
                    .status(response.getStatusCode())
                    .body(new ApiResponse(true, response.getBody(),
                            "Success from monolith", null, "success"));
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(new ApiResponse(false, null,
                            "Error from monolith", e.getResponseBodyAsString(), "error"));
        }
    }

    private ResponseEntity<String> makeRequest(String url, String method,
                                               String requestBody, String headers) {
        HttpMethod httpMethod = HttpMethod.valueOf(method.toUpperCase());
        HttpHeaders httpHeaders = new HttpHeaders();

        // Здесь можно парсить и добавлять заголовки из строки headers
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity;
        if (requestBody != null && !requestBody.isEmpty()) {
            entity = new HttpEntity<>(requestBody, httpHeaders);
        } else {
            entity = new HttpEntity<>(httpHeaders);
        }

        return restTemplate.exchange(url, httpMethod, entity, String.class);
    }
}

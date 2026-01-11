package com.cinemaabyss.proxy.service;

import com.cinemaabyss.proxy.dto.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface ProxyService {
    ResponseEntity<ApiResponse> proxyRequest(String path, String method,
                                             String requestBody, String headers);

    ResponseEntity<ApiResponse> proxyMoviesRequest(String path, String method,
                                                   String requestBody, String headers);

    ResponseEntity<ApiResponse> proxyEventsRequest(String path, String method,
                                                   String requestBody, String headers);
}

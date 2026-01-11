package com.cinemaabyss.proxy.controller;

import com.cinemaabyss.proxy.dto.ApiResponse;
import com.cinemaabyss.proxy.service.ProxyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProxyController {

    private final ProxyService proxyService;

    public ProxyController(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.POST,
            RequestMethod.PUT, RequestMethod.DELETE,
            RequestMethod.PATCH})
    public ResponseEntity<ApiResponse> proxyAllRequests(
            HttpServletRequest request,
            @RequestBody(required = false) String requestBody) {

        String path = request.getRequestURI();
        String method = request.getMethod();

        // Собираем заголовки в строку (упрощенный вариант)
        Map<String, String> headersMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headersMap.put(headerName, request.getHeader(headerName));
        }

        String headers = headersMap.toString();

        return proxyService.proxyRequest(path, method, requestBody, headers);
    }

    // Отдельные эндпоинты для управления фиче-флагами (опционально)
    @GetMapping("/admin/feature-flags")
    public ResponseEntity<ApiResponse> getFeatureFlags() {
        // Здесь можно вернуть текущее состояние фиче-флагов
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Feature flags status endpoint");
        return ResponseEntity.ok(new ApiResponse(true, data, "Success", null));
    }
}
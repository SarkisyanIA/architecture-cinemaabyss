package com.cinemaabyss.proxy.controller;

import com.cinemaabyss.proxy.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse> getFeatureFlags() {
        return ResponseEntity.ok(new ApiResponse(true, "API working", "Success", null, "success"));
    }
}

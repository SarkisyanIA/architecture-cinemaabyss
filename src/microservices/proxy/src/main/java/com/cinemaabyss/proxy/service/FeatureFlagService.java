package com.cinemaabyss.proxy.service;

import com.cinemaabyss.proxy.config.FeatureFlagConfig;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class FeatureFlagService {

    private final FeatureFlagConfig featureFlagConfig;
    private final Random random;

    public FeatureFlagService(FeatureFlagConfig featureFlagConfig) {
        this.featureFlagConfig = featureFlagConfig;
        this.random = new Random();
    }

    public boolean shouldRouteToNewService(String serviceName) {
        if (!featureFlagConfig.isGradualMigrationEnabled()) {
            return false; // По умолчанию весь трафик идет в монолит
        }

        switch (serviceName) {
            case "movies":
                return random.nextInt(100) < featureFlagConfig.getMoviesMigrationPercent();
            case "events":
                // Здесь можно добавить логику для events, когда начнется их миграция
                return false;
            default:
                return false;
        }
    }
}

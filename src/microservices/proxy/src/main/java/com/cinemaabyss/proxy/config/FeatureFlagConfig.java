package com.cinemaabyss.proxy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeatureFlagConfig {

    @Value("${gradual.migration:false}")
    private boolean gradualMigrationEnabled;

    @Value("${movies.migration.percent:0}")
    private int moviesMigrationPercent;

    public boolean isGradualMigrationEnabled() {
        return gradualMigrationEnabled;
    }

    public int getMoviesMigrationPercent() {
        return moviesMigrationPercent;
    }
}

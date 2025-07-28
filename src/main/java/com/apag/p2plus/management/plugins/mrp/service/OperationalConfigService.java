package com.apag.p2plus.management.plugins.mrp.service;

import com.apag.p2plus.management.plugins.mrp.model.ConfigItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service for loading operational configuration via RESTEasy Client
 */
public class OperationalConfigService extends BaseConfigService<ConfigItem> {

    /**
     * Loads operational configuration asynchronously from the REST API
     * 
     * @param scenarioId The scenario ID to load configuration for
     * @return CompletableFuture with the list of operational configuration items
     */
    public CompletableFuture<List<ConfigItem>> loadOperationalConfigAsync(String scenarioId) {
        return loadConfigAsync(() -> configClient.getOperationalConfig(scenarioId));
    }

    /**
     * Loads operational configuration synchronously from the REST API
     * 
     * @param scenarioId The scenario ID to load configuration for
     * @return List of operational configuration items
     */
    public List<ConfigItem> loadOperationalConfig(String scenarioId) {
        return loadConfig(() -> configClient.getOperationalConfig(scenarioId));
    }

    /**
     * Creates fallback operational configuration when the API is not available
     * 
     * @return List with example operational configuration items
     */
    @Override
    protected List<ConfigItem> createFallbackConfig() {
        List<ConfigItem> fallbackConfig = new ArrayList<>();
        fallbackConfig.add(new ConfigItem("batchSize", "string", "1000", "Batch size"));
        fallbackConfig.add(new ConfigItem("enableLogging", "boolean", true, "Enable logging"));
        fallbackConfig.add(new ConfigItem("retryCount", "string", "3", "Retry count"));
        return fallbackConfig;
    }
} 
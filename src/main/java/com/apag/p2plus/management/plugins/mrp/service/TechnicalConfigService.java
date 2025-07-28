package com.apag.p2plus.management.plugins.mrp.service;

import com.apag.p2plus.management.plugins.mrp.model.ConfigItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service for loading technical configuration via RESTEasy Client
 */
public class TechnicalConfigService extends BaseConfigService<ConfigItem> {

    /**
     * Loads technical configuration asynchronously from the REST API
     * 
     * @return CompletableFuture with the list of technical configuration items
     */
    public CompletableFuture<List<ConfigItem>> loadTechnicalConfigAsync() {
        return loadConfigAsync(() -> configClient.getTechnicalConfig());
    }

    /**
     * Loads technical configuration synchronously from the REST API
     * 
     * @return List of technical configuration items
     */
    public List<ConfigItem> loadTechnicalConfig() {
        return loadConfig(() -> configClient.getTechnicalConfig());
    }

    /**
     * Creates fallback technical configuration when the API is not available
     * 
     * @return List with example technical configuration items
     */
    @Override
    protected List<ConfigItem> createFallbackConfig() {
        List<ConfigItem> fallbackConfig = new ArrayList<>();
        fallbackConfig.add(new ConfigItem("datasourceUrl", "string", "jdbc:mssql://localhost:5432/mydb", "Datasource URL"));
        fallbackConfig.add(new ConfigItem("datasourceDriver", "string", "mssql", "Datasource driver"));
        fallbackConfig.add(new ConfigItem("datasourceUsername", "string", "admin", "Datasource username"));
        fallbackConfig.add(new ConfigItem("datasourcePassword", "password", null, "Datasource password"));
        fallbackConfig.add(new ConfigItem("datasourceDebug", "boolean", false, "Datasource debugging"));
        return fallbackConfig;
    }
} 
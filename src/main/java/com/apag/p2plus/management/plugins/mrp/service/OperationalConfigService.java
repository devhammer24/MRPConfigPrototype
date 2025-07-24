package com.apag.p2plus.management.plugins.mrp.service;

import com.apag.p2plus.management.plugins.mrp.model.ConfigItem;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service for loading operational configuration via REST API
 */
public class OperationalConfigService extends BaseConfigService<ConfigItem> {

  private static final String OPERATIONAL_CONFIG_URL = "https://80ab19d6-cfd5-42b4-889e-714dd9f0d184.mock.pstmn.io/config/operational/";

  /**
   * Loads operational configuration asynchronously from the REST API
   * 
   * @param scenarioId The scenario ID to load configuration for
   * @return CompletableFuture with the list of operational configuration items
   */
  public CompletableFuture<List<ConfigItem>> loadOperationalConfigAsync(String scenarioId) {
    String url = OPERATIONAL_CONFIG_URL + scenarioId;
    TypeReference<List<ConfigItem>> typeRef = new TypeReference<List<ConfigItem>>() {};
    return loadConfigAsync(url, typeRef);
  }

  /**
   * Loads operational configuration synchronously from the REST API
   * 
   * @param scenarioId The scenario ID to load configuration for
   * @return List of operational configuration items
   * @throws IOException on network errors
   */
  public List<ConfigItem> loadOperationalConfig(String scenarioId) throws IOException {
    String url = OPERATIONAL_CONFIG_URL + scenarioId;
    TypeReference<List<ConfigItem>> typeRef = new TypeReference<List<ConfigItem>>() {};
    return loadConfig(url, typeRef);
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
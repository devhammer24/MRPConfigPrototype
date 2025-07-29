package com.apag.p2plus.management.plugins.mrp.service;

import com.apag.p2plus.management.plugins.mrp.model.ConfigItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service for loading operational configuration
 */
public class OperationalConfigService extends BaseConfigService<ConfigItem> {

  @Override
  public List<ConfigItem> load() {
    throw new UnsupportedOperationException("Use load(String scenarioId) instead");
  }

  @Override
  public CompletableFuture<List<ConfigItem>> loadAsync() {
    throw new UnsupportedOperationException("Use loadAsync(String scenarioId) instead");
  }

  /**
   * Loads operational configuration for a specific scenario
   */
  public List<ConfigItem> load(String scenarioId) {
    return loadConfig(() -> configClient.getOperationalConfig(scenarioId));
  }

  /**
   * Loads operational configuration asynchronously for a specific scenario
   */
  public CompletableFuture<List<ConfigItem>> loadAsync(String scenarioId) {
    return loadConfigAsync(() -> configClient.getOperationalConfig(scenarioId));
  }

  @Override
  protected List<ConfigItem> createFallbackConfig() {
    List<ConfigItem> fallbackConfig = new ArrayList<>();
    fallbackConfig.add(new ConfigItem("batchSize", "string", "1000", "Batch size"));
    fallbackConfig.add(new ConfigItem("enableLogging", "boolean", true, "Enable logging"));
    fallbackConfig.add(new ConfigItem("retryCount", "string", "3", "Retry count"));
    return fallbackConfig;
  }
} 
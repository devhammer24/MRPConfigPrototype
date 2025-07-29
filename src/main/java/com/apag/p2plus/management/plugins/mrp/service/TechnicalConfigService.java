package com.apag.p2plus.management.plugins.mrp.service;

import com.apag.p2plus.management.plugins.mrp.model.ConfigItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service for loading technical configuration
 */
public class TechnicalConfigService extends BaseConfigService<ConfigItem> {

  @Override
  public List<ConfigItem> load() {
    return loadConfig(() -> configClient.getTechnicalConfig());
  }

  @Override
  public CompletableFuture<List<ConfigItem>> loadAsync() {
    return loadConfigAsync(() -> configClient.getTechnicalConfig());
  }

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
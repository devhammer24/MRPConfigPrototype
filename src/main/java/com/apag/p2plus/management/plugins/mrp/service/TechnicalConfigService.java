package com.apag.p2plus.management.plugins.mrp.service;

import com.apag.p2plus.management.plugins.mrp.model.ConfigItem;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service for loading technical configuration via REST API
 */
public class TechnicalConfigService extends BaseConfigService<ConfigItem> {

  private static final String TECHNICAL_CONFIG_URL = "https://80ab19d6-cfd5-42b4-889e-714dd9f0d184.mock.pstmn.io/config/technical";

  /**
   * Loads technical configuration asynchronously from the REST API
   * 
   * @return CompletableFuture with the list of technical configuration items
   */
  public CompletableFuture<List<ConfigItem>> loadTechnicalConfigAsync() {
    TypeReference<List<ConfigItem>> typeRef = new TypeReference<List<ConfigItem>>() {};
    return loadConfigAsync(TECHNICAL_CONFIG_URL, typeRef);
  }

  /**
   * Loads technical configuration synchronously from the REST API
   * 
   * @return List of technical configuration items
   * @throws IOException on network errors
   */
  public List<ConfigItem> loadTechnicalConfig() throws IOException {
    TypeReference<List<ConfigItem>> typeRef = new TypeReference<List<ConfigItem>>() {};
    return loadConfig(TECHNICAL_CONFIG_URL, typeRef);
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
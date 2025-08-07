package com.apag.p2plus.management.plugins.mrp.service;

import com.apag.p2plus.management.plugins.mrp.model.ConfigItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service for loading and saving technical configuration
 */
public class TechnicalConfigService extends BaseConfigService<ConfigItem> {

  private static final Logger logger = LoggerFactory.getLogger(TechnicalConfigService.class);

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

  /**
   * Saves technical configuration items
   * 
   * @param configItems List of configuration items to save
   */
  public void saveTechnicalConfig(List<ConfigItem> configItems) {
    try {
      logger.info("Saving {} technical configuration items", configItems.size());
      configClient.saveTechnicalConfig(configItems);
      logger.info("Technical configuration saved successfully");
    } catch (Exception e) {
      logger.error("Error saving technical configuration", e);
      throw new RuntimeException("Failed to save technical configuration: " + e.getMessage(), e);
    }
  }
}
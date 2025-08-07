package com.apag.p2plus.management.plugins.mrp.service;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * Base service for configuration loading via RESTEasy Client
 */
public abstract class BaseConfigService<T> {

  private static final Logger logger = LoggerFactory.getLogger(BaseConfigService.class);
  private static final String BASE_URL = "https://80ab19d6-cfd5-42b4-889e-714dd9f0d184.mock.pstmn.io";

  protected final Client client;
  protected final MRPConfigClient configClient;

  public BaseConfigService() {
    // RESTEasy Client - Jackson is automatically detected
    this.client = ClientBuilder.newBuilder().build();
    
    // Create proxy for the MRP Config API
    ResteasyWebTarget target = (ResteasyWebTarget) client.target(BASE_URL);
    this.configClient = target.proxy(MRPConfigClient.class);
  }

  /**
   * Loads configuration items synchronously
   */
  public abstract List<T> load();

  /**
   * Loads configuration items asynchronously
   */
  public abstract CompletableFuture<List<T>> loadAsync();

  /**
   * Generic method to load configuration asynchronously with fallback
   */
  protected CompletableFuture<List<T>> loadConfigAsync(Supplier<List<T>> configSupplier) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        return configSupplier.get();
      } catch (Exception e) {
        logger.warn("API error, using fallback: {}", e.getMessage());
        return createFallbackConfig();
      }
    });
  }

  /**
   * Generic method to load configuration synchronously with fallback
   */
  protected List<T> loadConfig(Supplier<List<T>> configSupplier) {
    try {
      return configSupplier.get();
    } catch (Exception e) {
      logger.warn("API error, using fallback: {}", e.getMessage());
      return createFallbackConfig();
    }
  }

  /**
   * Creates fallback configuration when API is not available
   */
  protected abstract List<T> createFallbackConfig();

  /**
   * Saves configuration items
   */
  public void save(List<T> items) {
    logger.info("Saving {} configuration items", items.size());
    // TODO: Implement save functionality when API supports it
  }

  /**
   * Closes the client
   */
  public void close() {
    if (client != null) {
      try {
        client.close();
      } catch (Exception e) {
        logger.warn("Error closing client: {}", e.getMessage());
      }
    }
  }
}
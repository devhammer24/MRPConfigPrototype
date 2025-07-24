package com.apag.p2plus.management.plugins.mrp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Abstract base service for configuration loading via REST API
 */
public abstract class BaseConfigService<T> {

  protected final ObjectMapper objectMapper;
  protected final CloseableHttpClient httpClient;

  public BaseConfigService() {
    this.objectMapper = new ObjectMapper();
    this.httpClient = HttpClients.createDefault();
  }

  /**
   * Generic method to load configuration asynchronously
   * 
   * @param url The URL to call
   * @param typeRef TypeReference for JSON deserialization
   * @return CompletableFuture with the list of configuration items
   */
  protected CompletableFuture<List<T>> loadConfigAsync(String url, TypeReference<List<T>> typeRef) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        return loadConfig(url, typeRef);
      } catch (Exception e) {
        System.err.println("Error loading configuration from " + url + ": " + e.getMessage());
        return createFallbackConfig();
      }
    });
  }

  /**
   * Generic method to load configuration synchronously
   * 
   * @param url The URL to call
   * @param typeRef TypeReference for JSON deserialization
   * @return List of configuration items
   * @throws IOException on network errors
   */
  protected List<T> loadConfig(String url, TypeReference<List<T>> typeRef) throws IOException {
    HttpGet request = new HttpGet(url);
    request.setHeader("Accept", "application/json");

    return httpClient.execute(request, response -> {
      String jsonResponse = EntityUtils.toString(response.getEntity());

      if (response.getCode() == 200) {
        return objectMapper.readValue(jsonResponse, typeRef);
      } else {
        throw new IOException("HTTP error: " + response.getCode());
      }
    });
  }

  /**
   * Abstract method to create fallback configuration when API is not available
   * 
   * @return List with fallback configuration items
   */
  protected abstract List<T> createFallbackConfig();

  /**
   * Closes the HTTP client
   */
  public void close() {
    try {
      httpClient.close();
    } catch (IOException e) {
      System.err.println("Error closing HTTP client: " + e.getMessage());
    }
  }
} 
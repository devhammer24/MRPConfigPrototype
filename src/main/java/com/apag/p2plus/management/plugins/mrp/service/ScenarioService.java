package com.apag.p2plus.management.plugins.mrp.service;

import com.apag.p2plus.management.plugins.mrp.model.Scenario;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service for loading and creating scenarios
 */
public class ScenarioService extends BaseConfigService<Scenario> {

  private static final Logger logger = LoggerFactory.getLogger(ScenarioService.class);

  @Override
  public List<Scenario> load() {
    return loadConfig(() -> configClient.getScenarios());
  }

  @Override
  public CompletableFuture<List<Scenario>> loadAsync() {
    return loadConfigAsync(() -> configClient.getScenarios());
  }

  @Override
  protected List<Scenario> createFallbackConfig() {
    List<Scenario> fallbackScenarios = new ArrayList<>();
    fallbackScenarios.add(new Scenario("Standard_LDL_M1000", "Production run Client 1000"));
    fallbackScenarios.add(new Scenario("Test_Mandant_3000", "Test scenario for Client 3000"));
    return fallbackScenarios;
  }

  /**
   * Creates a new scenario
   * 
   * @param scenario Scenario object to create
   * @return true if scenario was created successfully (HTTP 201), false otherwise
   */
  public boolean createScenario(Scenario scenario) {
    try {
      logger.info("Creating new scenario: {}", scenario.getScenarioId());
      
      try (Response response = configClient.createScenario(scenario)) {
        int statusCode = response.getStatus();
        logger.info("Create scenario response status: {}", statusCode);
        
        if (statusCode == 201) {
          logger.info("Scenario '{}' created successfully", scenario.getScenarioId());
          return true;
        } else {
          logger.warn("Failed to create scenario '{}', status code: {}", scenario.getScenarioId(), statusCode);
          return false;
        }
      }
      
    } catch (Exception e) {
      logger.error("Error creating scenario '{}'", scenario.getScenarioId(), e);
      throw new RuntimeException("Failed to create scenario: " + e.getMessage(), e);
    }
  }
}
package com.apag.p2plus.management.plugins.mrp.service;

import com.apag.p2plus.management.plugins.mrp.model.Scenario;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service for loading scenarios via REST API
 */
public class ScenarioService extends BaseConfigService<Scenario> {

  private static final String SCENARIOS_URL = "https://80ab19d6-cfd5-42b4-889e-714dd9f0d184.mock.pstmn.io/config/scenarios";

  /**
   * Loads scenarios asynchronously from the REST API
   * 
   * @return CompletableFuture with the list of scenarios
   */
  public CompletableFuture<List<Scenario>> loadScenariosAsync() {
    TypeReference<List<Scenario>> typeRef = new TypeReference<List<Scenario>>() {};
    return loadConfigAsync(SCENARIOS_URL, typeRef);
  }

  /**
   * Loads scenarios synchronously from the REST API
   * 
   * @return List of scenarios
   * @throws IOException on network errors
   */
  public List<Scenario> loadScenarios() throws IOException {
    TypeReference<List<Scenario>> typeRef = new TypeReference<List<Scenario>>() {};
    return loadConfig(SCENARIOS_URL, typeRef);
  }

  /**
   * Creates fallback scenarios when the API is not available
   * 
   * @return List with example scenarios
   */
  @Override
  protected List<Scenario> createFallbackConfig() {
    List<Scenario> fallbackScenarios = new ArrayList<>();
    fallbackScenarios.add(new Scenario("Standard_LDL_M1000", "Production run Client 1000"));
    fallbackScenarios.add(new Scenario("Test_Mandant_3000", "Test scenario for Client 3000"));
    return fallbackScenarios;
  }
} 
package com.apag.p2plus.management.plugins.mrp.service;

import com.apag.p2plus.management.plugins.mrp.model.Scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service for loading scenarios
 */
public class ScenarioService extends BaseConfigService<Scenario> {

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
} 
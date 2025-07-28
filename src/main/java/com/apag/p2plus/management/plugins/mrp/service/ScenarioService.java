package com.apag.p2plus.management.plugins.mrp.service;

import com.apag.p2plus.management.plugins.mrp.model.Scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service for loading scenarios via RESTEasy Client
 */
public class ScenarioService extends BaseConfigService<Scenario> {

    /**
     * Loads scenarios asynchronously from the REST API
     * 
     * @return CompletableFuture with the list of scenarios
     */
    public CompletableFuture<List<Scenario>> loadScenariosAsync() {
        return loadConfigAsync(() -> configClient.getScenarios());
    }

    /**
     * Loads scenarios synchronously from the REST API
     * 
     * @return List of scenarios
     */
    public List<Scenario> loadScenarios() {
        return loadConfig(() -> configClient.getScenarios());
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
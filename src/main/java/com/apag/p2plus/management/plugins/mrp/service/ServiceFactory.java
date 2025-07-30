package com.apag.p2plus.management.plugins.mrp.service;

/**
 * Factory for creating service instances for the MRP application.
 */
public class ServiceFactory {

  private static ScenarioService scenarioService;
  private static TechnicalConfigService technicalConfigService;
  private static OperationalConfigService operationalConfigService;

  private ServiceFactory() {
    // prevent instantiation
  }

  public static ScenarioService getScenarioService() {
    if (scenarioService == null) {
      scenarioService = new ScenarioService();
    }
    return scenarioService;
  }

  public static TechnicalConfigService getTechnicalConfigService() {
    if (technicalConfigService == null) {
      technicalConfigService = new TechnicalConfigService();
    }
    return technicalConfigService;
  }

  public static OperationalConfigService getOperationalConfigService() {
    if (operationalConfigService == null) {
      operationalConfigService = new OperationalConfigService();
    }
    return operationalConfigService;
  }
}
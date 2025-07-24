package com.apag.p2plus.management.plugins.mrp.model;

/**
 * Represents a scenario with ID and description
 */
public class Scenario {

  private String scenarioId;
  private String description;

  /**
   * Default constructor for JSON deserialization
   */
  public Scenario() {
  }

  public Scenario(String scenarioId, String description) {
    this.scenarioId = scenarioId;
    this.description = description;
  }

  public String getScenarioId() {
    return scenarioId;
  }

  public void setScenarioId(String scenarioId) {
    this.scenarioId = scenarioId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return description;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Scenario scenario = (Scenario) obj;
    return scenarioId != null ? scenarioId.equals(scenario.scenarioId) : scenario.scenarioId == null;
  }

  @Override
  public int hashCode() {
    return scenarioId != null ? scenarioId.hashCode() : 0;
  }
} 
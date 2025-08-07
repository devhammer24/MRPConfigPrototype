package com.apag.p2plus.management.plugins.mrp.service;

import com.apag.p2plus.management.plugins.mrp.model.ConfigItem;
import com.apag.p2plus.management.plugins.mrp.model.Scenario;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * JAX-RS Client Interface for MRP Configuration APIs
 */
@Path("/config")
@Produces(MediaType.APPLICATION_JSON)
public interface MRPConfigClient {

  /**
   * Loads all available scenarios
   * 
   * @return List of scenarios
   */
  @GET
  @Path("/scenarios")
  List<Scenario> getScenarios();

  /**
   * Loads technical configuration items
   * 
   * @return List of technical configuration items
   */
  @GET
  @Path("/technical")
  List<ConfigItem> getTechnicalConfig();

  /**
   * Loads operational configuration items for a specific scenario
   * 
   * @param scenarioId ID of the scenario
   * @return List of operational configuration items
   */
  @GET
  @Path("/operational/{scenarioId}")
  List<ConfigItem> getOperationalConfig(@PathParam("scenarioId") String scenarioId);

  /**
   * Saves technical configuration items
   * 
   * @param configItems List of configuration items to save
   */
  @PUT
  @Path("/technical")
  @Consumes(MediaType.APPLICATION_JSON)
  void saveTechnicalConfig(List<ConfigItem> configItems);

  /**
   * Creates a new scenario
   * 
   * @param scenario Scenario object to create
   * @return Response with HTTP status code
   */
  @POST
  @Path("/scenarios")
  @Consumes(MediaType.APPLICATION_JSON)
  Response createScenario(Scenario scenario);
}
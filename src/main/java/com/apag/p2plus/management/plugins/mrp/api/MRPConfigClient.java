package com.apag.p2plus.management.plugins.mrp.api;

import com.apag.p2plus.management.plugins.mrp.model.ConfigItem;
import com.apag.p2plus.management.plugins.mrp.model.Scenario;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

/**
 * JAX-RS Client Interface für MRP Configuration APIs
 */
@Path("/config")
@Produces(MediaType.APPLICATION_JSON)
public interface MRPConfigClient {

    /**
     * Lädt alle verfügbaren Szenarien
     * 
     * @return Liste von Szenarien
     */
    @GET
    @Path("/scenarios")
    List<Scenario> getScenarios();

    /**
     * Lädt technische Konfigurationselemente
     * 
     * @return Liste von technischen Konfigurationselementen
     */
    @GET
    @Path("/technical")
    List<ConfigItem> getTechnicalConfig();

    /**
     * Lädt operationale Konfigurationselemente für ein bestimmtes Szenario
     * 
     * @param scenarioId ID des Szenarios
     * @return Liste von operationalen Konfigurationselementen
     */
    @GET
    @Path("/operational/{scenarioId}")
    List<ConfigItem> getOperationalConfig(@PathParam("scenarioId") String scenarioId);
} 
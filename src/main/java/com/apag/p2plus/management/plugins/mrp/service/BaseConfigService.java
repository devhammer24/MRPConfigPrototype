package com.apag.p2plus.management.plugins.mrp.service;

import com.apag.p2plus.management.plugins.mrp.api.MRPConfigClient;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * Abstract base service for configuration loading via RESTEasy Client
 */
public abstract class BaseConfigService<T> {

    private static final String BASE_URL = "https://80ab19d6-cfd5-42b4-889e-714dd9f0d184.mock.pstmn.io";
    
    protected final Client client;
    protected final MRPConfigClient configClient;

    public BaseConfigService() {
        // RESTEasy Client mit Jackson2Provider konfigurieren
        this.client = ClientBuilder.newBuilder()
                .register(ResteasyJackson2Provider.class)
                .build();
                
        // Proxy für die MRP Config API erstellen
        ResteasyWebTarget target = (ResteasyWebTarget) client.target(BASE_URL);
        this.configClient = target.proxy(MRPConfigClient.class);
    }

    /**
     * Generic method to load configuration asynchronously with fallback
     * 
     * @param configSupplier Supplier-Funktion für die API-Abfrage
     * @return CompletableFuture with the list of configuration items
     */
    protected CompletableFuture<List<T>> loadConfigAsync(Supplier<List<T>> configSupplier) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return configSupplier.get();
            } catch (Exception e) {
                System.err.println("Error loading configuration: " + e.getMessage());
                return createFallbackConfig();
            }
        });
    }

    /**
     * Generic method to load configuration synchronously with fallback
     * 
     * @param configSupplier Supplier-Funktion für die API-Abfrage
     * @return List of configuration items
     */
    protected List<T> loadConfig(Supplier<List<T>> configSupplier) {
        try {
            return configSupplier.get();
        } catch (Exception e) {
            System.err.println("Error loading configuration: " + e.getMessage());
            return createFallbackConfig();
        }
    }

    /**
     * Abstract method to create fallback configuration when API is not available
     * 
     * @return List with fallback configuration items
     */
    protected abstract List<T> createFallbackConfig();

    /**
     * Closes the RESTEasy client
     */
    public void close() {
        if (client != null) {
            client.close();
        }
    }
} 
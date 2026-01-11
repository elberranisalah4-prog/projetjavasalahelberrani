package com.municipal.dashboard.dao;

import com.municipal.dashboard.Consommation;
import com.municipal.dashboard.service.AsyncDatabaseService;
import com.municipal.dashboard.util.StreamUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * DAO asynchrone pour les consommations utilisant streams et threads
 */
public class ConsommationDAOAsync {
    private ConsommationDAO consommationDAO;
    private AsyncDatabaseService asyncService;
    
    public ConsommationDAOAsync() {
        this.consommationDAO = new ConsommationDAO();
        this.asyncService = new AsyncDatabaseService();
    }
    
    /**
     * Récupère toutes les consommations de manière asynchrone
     */
    public CompletableFuture<List<Consommation>> findAllAsync() {
        return asyncService.readAsync(consommationDAO::findAll);
    }
    
    /**
     * Récupère toutes les consommations avec callback
     */
    public void findAllAsync(Consumer<List<Consommation>> onSuccess,
                              Consumer<Exception> onError) {
        asyncService.readAsync(consommationDAO::findAll, onSuccess, onError);
    }
    
    /**
     * Insère une consommation de manière asynchrone
     */
    public CompletableFuture<Void> insertAsync(Consommation consommation) {
        return asyncService.writeAsync(() -> consommationDAO.insert(consommation));
    }
    
    /**
     * Insère une consommation avec callback
     */
    public void insertAsync(Consommation consommation,
                           Runnable onSuccess,
                           Consumer<Exception> onError) {
        asyncService.writeAsync(
            () -> consommationDAO.insert(consommation),
            onSuccess,
            onError
        );
    }
    
    /**
     * Filtre les consommations par zone de manière asynchrone
     */
    public CompletableFuture<List<Consommation>> findByZoneAsync(String zone) {
        return findAllAsync()
                .thenApply(consommations -> 
                    StreamUtils.filter(
                        consommations,
                        c -> zone != null && zone.equals(c.getZone())
                    )
                );
    }
    
    /**
     * Filtre les consommations par type de service de manière asynchrone
     */
    public CompletableFuture<List<Consommation>> findByServiceTypeAsync(String serviceType) {
        return findAllAsync()
                .thenApply(consommations -> 
                    StreamUtils.filter(
                        consommations,
                        c -> serviceType != null && serviceType.equals(c.getServiceType())
                    )
                );
    }
    
    /**
     * Calcule les statistiques par zone de manière asynchrone
     */
    public CompletableFuture<Map<String, Double>> getStatisticsByZoneAsync() {
        return findAllAsync()
                .thenApply(consommations -> 
                    StreamUtils.sumBy(
                        consommations,
                        Consommation::getZone,
                        Consommation::getValeur
                    )
                );
    }
    
    /**
     * Trouve les consommations supérieures à un seuil de manière asynchrone
     */
    public CompletableFuture<List<Consommation>> findAboveThresholdAsync(double threshold) {
        return findAllAsync()
                .thenApply(consommations -> 
                    StreamUtils.filter(
                        consommations,
                        c -> c.getValeur() != null && c.getValeur() > threshold
                    )
                );
    }
    
    /**
     * Calcule la consommation totale de manière asynchrone
     */
    public CompletableFuture<Double> getTotalAsync() {
        return findAllAsync()
                .thenApply(consommations -> 
                    StreamUtils.sum(consommations, Consommation::getValeur)
                );
    }
    
    /**
     * Calcule la consommation moyenne de manière asynchrone
     */
    public CompletableFuture<Double> getAverageAsync() {
        return findAllAsync()
                .thenApply(consommations -> 
                    StreamUtils.average(consommations, Consommation::getValeur)
                            .orElse(0.0)
                );
    }
    
    /**
     * Filtre par période de manière asynchrone
     */
    public CompletableFuture<List<Consommation>> findByDateRangeAsync(LocalDateTime start,
                                                                      LocalDateTime end) {
        return findAllAsync()
                .thenApply(consommations -> 
                    StreamUtils.filter(
                        consommations,
                        c -> {
                            if (c.getDate() == null || start == null || end == null) {
                                return false;
                            }
                            LocalDateTime date = c.getDate();
                            return !date.isBefore(start) && !date.isAfter(end);
                        }
                    )
                );
    }
    
    /**
     * Trouve les top N consommations de manière asynchrone
     */
    public CompletableFuture<List<Consommation>> getTopNAsync(int n) {
        return findAllAsync()
                .thenApply(consommations -> {
                    List<Consommation> sorted = StreamUtils.sort(
                        consommations,
                        (c1, c2) -> {
                            Double v1 = c1.getValeur() != null ? c1.getValeur() : 0.0;
                            Double v2 = c2.getValeur() != null ? c2.getValeur() : 0.0;
                            return Double.compare(v2, v1); // Ordre décroissant
                        }
                    );
                    return StreamUtils.limit(sorted, n);
                });
    }
}


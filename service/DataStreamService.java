package com.municipal.dashboard.service;

import com.municipal.dashboard.Consommation;
import com.municipal.dashboard.Production;
import com.municipal.dashboard.util.StreamUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service pour traiter les données avec des streams
 * Fournit des méthodes pratiques pour analyser les données municipales
 */
public class DataStreamService {
    
    /**
     * Calcule les statistiques de consommation par zone
     */
    public Map<String, Double> getConsommationByZone(List<Consommation> consommations) {
        return StreamUtils.sumBy(
            consommations,
            Consommation::getZone,
            Consommation::getValeur
        );
    }
    
    /**
     * Calcule la consommation moyenne par type de service
     */
    public Map<String, Double> getConsommationMoyenneByService(List<Consommation> consommations) {
        return StreamUtils.averageBy(
            consommations,
            Consommation::getServiceType,
            Consommation::getValeur
        );
    }
    
    /**
     * Trouve les consommations supérieures à un seuil
     */
    public List<Consommation> getConsommationsAboveThreshold(List<Consommation> consommations, double threshold) {
        return StreamUtils.filter(
            consommations,
            c -> c.getValeur() != null && c.getValeur() > threshold
        );
    }
    
    /**
     * Groupe les consommations par zone
     */
    public Map<String, List<Consommation>> groupConsommationsByZone(List<Consommation> consommations) {
        return StreamUtils.groupBy(consommations, Consommation::getZone);
    }
    
    /**
     * Calcule les statistiques de production par zone
     */
    public Map<String, Double> getProductionByZone(List<Production> productions) {
        return StreamUtils.sumBy(
            productions,
            Production::getZone,
            Production::getValeur
        );
    }
    
    /**
     * Trouve les productions maximales par zone
     */
    public Map<String, Optional<Production>> getMaxProductionByZone(List<Production> productions) {
        return productions.stream()
                .collect(Collectors.groupingBy(
                    Production::getZone,
                    Collectors.maxBy(Comparator.comparing(Production::getValeur))
                ));
    }
    
    /**
     * Filtre les données par période
     */
    public <T> List<T> filterByDateRange(List<T> data,
                                         Function<T, LocalDateTime> dateExtractor,
                                         LocalDateTime start,
                                         LocalDateTime end) {
        return data.stream()
                .filter(item -> {
                    LocalDateTime date = dateExtractor.apply(item);
                    return date != null && !date.isBefore(start) && !date.isAfter(end);
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Calcule le total de consommation sur une période
     */
    public double getTotalConsommationInPeriod(List<Consommation> consommations,
                                               LocalDateTime start,
                                               LocalDateTime end) {
        List<Consommation> filtered = filterByDateRange(
            consommations,
            Consommation::getDate,
            start,
            end
        );
        return StreamUtils.sum(filtered, Consommation::getValeur);
    }
    
    /**
     * Trouve les top N consommations
     */
    public List<Consommation> getTopConsommations(List<Consommation> consommations, int n) {
        return consommations.stream()
                .sorted(Comparator.comparing(Consommation::getValeur).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }
    
    /**
     * Calcule les statistiques complètes
     */
    public Map<String, Object> getStatistics(List<Consommation> consommations) {
        Map<String, Object> stats = new HashMap<>();
        
        if (consommations.isEmpty()) {
            return stats;
        }
        
        stats.put("total", StreamUtils.sum(consommations, Consommation::getValeur));
        stats.put("moyenne", StreamUtils.average(consommations, Consommation::getValeur).orElse(0.0));
        stats.put("max", StreamUtils.max(consommations, Comparator.comparing(Consommation::getValeur)));
        stats.put("min", StreamUtils.min(consommations, Comparator.comparing(Consommation::getValeur)));
        stats.put("count", consommations.size());
        
        return stats;
    }
    
    /**
     * Trouve les zones avec consommation anormale (écart-type)
     */
    public List<String> findAnomalousZones(List<Consommation> consommations) {
        // Calcule la moyenne globale
        double moyenne = StreamUtils.average(consommations, Consommation::getValeur)
                .orElse(0.0);
        
        // Calcule l'écart-type
        double variance = consommations.stream()
                .mapToDouble(c -> Math.pow((c.getValeur() - moyenne), 2))
                .average()
                .orElse(0.0);
        double ecartType = Math.sqrt(variance);
        
        // Trouve les zones avec consommation > moyenne + 2*écart-type
        double seuil = moyenne + 2 * ecartType;
        
        return consommations.stream()
                .filter(c -> c.getValeur() > seuil)
                .map(Consommation::getZone)
                .distinct()
                .collect(Collectors.toList());
    }
}


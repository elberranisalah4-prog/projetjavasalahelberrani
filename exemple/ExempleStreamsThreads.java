package com.municipal.dashboard.exemple;

import com.municipal.dashboard.Consommation;
import com.municipal.dashboard.Production;
import com.municipal.dashboard.dao.ConsommationDAO;
import com.municipal.dashboard.dao.ProductionDAO;
import com.municipal.dashboard.service.AsyncDatabaseService;
import com.municipal.dashboard.service.DataStreamService;
import com.municipal.dashboard.util.StreamUtils;
import com.municipal.dashboard.util.ThreadPoolManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Exemples d'utilisation des streams et threads
 */
public class ExempleStreamsThreads {
    
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("    EXEMPLES D'UTILISATION: STREAMS ET THREADS");
        System.out.println("=".repeat(70));
        System.out.println();
        
        // Exemple 1: Utilisation des streams
        exempleStreams();
        
        // Exemple 2: Utilisation des threads
        exempleThreads();
        
        // Exemple 3: Combinaison streams + threads
        exempleStreamsEtThreads();
        
        // Arrêt propre du ThreadPoolManager
        ThreadPoolManager.getInstance().shutdown();
    }
    
    /**
     * Exemple 1: Utilisation des streams pour traiter les données
     */
    private static void exempleStreams() {
        System.out.println("\n" + "-".repeat(70));
        System.out.println("EXEMPLE 1: UTILISATION DES STREAMS");
        System.out.println("-".repeat(70));
        
        ConsommationDAO consommationDAO = new ConsommationDAO();
        List<Consommation> consommations = consommationDAO.findAll();
        
        System.out.println("\n[1] Filtrage des consommations > 100:");
        List<Consommation> grandesConsommations = StreamUtils.filter(
            consommations,
            c -> c.getValeur() != null && c.getValeur() > 100
        );
        grandesConsommations.forEach(c -> 
            System.out.println("  - Zone: " + c.getZone() + ", Valeur: " + c.getValeur())
        );
        
        System.out.println("\n[2] Groupement par zone:");
        Map<String, List<Consommation>> parZone = StreamUtils.groupBy(
            consommations,
            Consommation::getZone
        );
        parZone.forEach((zone, liste) -> 
            System.out.println("  - Zone " + zone + ": " + liste.size() + " consommations")
        );
        
        System.out.println("\n[3] Calcul de la somme totale:");
        Double total = StreamUtils.sum(consommations, Consommation::getValeur);
        System.out.println("  - Total: " + total);
        
        System.out.println("\n[4] Calcul de la moyenne:");
        StreamUtils.average(consommations, Consommation::getValeur)
            .ifPresent(moyenne -> 
                System.out.println("  - Moyenne: " + moyenne)
            );
        
        System.out.println("\n[5] Utilisation du service DataStreamService:");
        DataStreamService streamService = new DataStreamService();
        Map<String, Double> consommationParZone = streamService.getConsommationByZone(consommations);
        consommationParZone.forEach((zone, somme) -> 
            System.out.println("  - Zone " + zone + ": " + somme)
        );
    }
    
    /**
     * Exemple 2: Utilisation des threads pour opérations asynchrones
     */
    private static void exempleThreads() {
        System.out.println("\n" + "-".repeat(70));
        System.out.println("EXEMPLE 2: UTILISATION DES THREADS");
        System.out.println("-".repeat(70));
        
        ThreadPoolManager threadManager = ThreadPoolManager.getInstance();
        AsyncDatabaseService asyncService = new AsyncDatabaseService();
        
        ConsommationDAO consommationDAO = new ConsommationDAO();
        ProductionDAO productionDAO = new ProductionDAO();
        
        System.out.println("\n[1] Lecture asynchrone des consommations:");
        CompletableFuture<List<Consommation>> futureConsommations = asyncService.readAsync(
            consommationDAO::findAll
        );
        
        futureConsommations.thenAccept(consommations -> {
            System.out.println("  ✓ Consommations chargées: " + consommations.size() + " éléments");
            System.out.println("  Thread: " + Thread.currentThread().getName());
        });
        
        System.out.println("\n[2] Lecture asynchrone avec callback:");
        asyncService.readAsync(
            productionDAO::findAll,
            productions -> {
                System.out.println("  ✓ Productions chargées: " + productions.size() + " éléments");
                System.out.println("  Thread: " + Thread.currentThread().getName());
            },
            error -> {
                System.err.println("  ✗ Erreur: " + error.getMessage());
            }
        );
        
        System.out.println("\n[3] Exécution de plusieurs opérations en parallèle:");
        CompletableFuture<List<Object>> allResults = asyncService.executeAllAsync(
            consommationDAO::findAll,
            productionDAO::findAll
        );
        
        allResults.thenAccept(results -> {
            System.out.println("  ✓ Toutes les opérations terminées");
            System.out.println("  Résultats: " + results.size() + " listes");
        });
        
        // Attendre la fin des opérations
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\n[4] Statistiques du pool de threads:");
        System.out.println("  " + threadManager.getPoolStats());
    }
    
    /**
     * Exemple 3: Combinaison streams et threads
     */
    private static void exempleStreamsEtThreads() {
        System.out.println("\n" + "-".repeat(70));
        System.out.println("EXEMPLE 3: COMBINAISON STREAMS + THREADS");
        System.out.println("-".repeat(70));
        
        AsyncDatabaseService asyncService = new AsyncDatabaseService();
        DataStreamService streamService = new DataStreamService();
        ConsommationDAO consommationDAO = new ConsommationDAO();
        
        System.out.println("\n[1] Chargement asynchrone puis traitement avec streams:");
        asyncService.readAsync(
            consommationDAO::findAll,
            consommations -> {
                System.out.println("  ✓ Données chargées dans le thread: " + Thread.currentThread().getName());
                
                // Traitement avec streams dans le thread
                Map<String, Double> stats = streamService.getConsommationByZone(consommations);
                System.out.println("  ✓ Statistiques calculées:");
                stats.forEach((zone, somme) -> 
                    System.out.println("    - " + zone + ": " + somme)
                );
            },
            error -> System.err.println("  ✗ Erreur: " + error.getMessage())
        );
        
        System.out.println("\n[2] Traitement parallèle avec streams:");
        CompletableFuture<List<Consommation>> future = asyncService.readAsync(
            consommationDAO::findAll
        );
        
        future.thenApply(consommations -> {
            // Traitement en parallèle
            return StreamUtils.parallelMap(
                consommations,
                c -> {
                    // Simulation d'un traitement lourd
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return c;
                }
            );
        }).thenAccept(processed -> {
            System.out.println("  ✓ " + processed.size() + " éléments traités en parallèle");
            System.out.println("  Thread: " + Thread.currentThread().getName());
        });
        
        // Attendre la fin
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}



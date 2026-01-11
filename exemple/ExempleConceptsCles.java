package com.municipal.dashboard.exemple;

import com.municipal.dashboard.Consommation;
import com.municipal.dashboard.dao.ConsommationDAO;
import com.municipal.dashboard.util.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Exemples d'utilisation des concepts clés Java :
 * 1. Génériques
 * 2. Collections
 * 3. Streams & Lambdas
 * 4. Optional
 */
public class ExempleConceptsCles {
    
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("    EXEMPLES DES CONCEPTS CLÉS JAVA");
        System.out.println("=".repeat(70));
        System.out.println();
        
        // 1. Génériques
        exempleGeneriques();
        
        // 2. Collections
        exempleCollections();
        
        // 3. Streams & Lambdas
        exempleStreamsLambdas();
        
        // 4. Optional
        exempleOptional();
        
        // 5. Combinaison de tous les concepts
        exempleCombinaison();
    }
    
    /**
     * EXEMPLE 1: Génériques
     * Assurent la sécurité de type - Le gestionnaire ne manipulera que des objets Consommation
     */
    private static void exempleGeneriques() {
        System.out.println("\n" + "-".repeat(70));
        System.out.println("1. GÉNÉRIQUES - Sécurité de type");
        System.out.println("-".repeat(70));
        
        // Création d'un gestionnaire générique pour Consommation
        // Le type est garanti: seules les Consommation peuvent être ajoutées
        GenericManager<Consommation> manager = new GenericManager<>(Consommation::getId);
        
        // Création de quelques consommations
        Consommation c1 = new Consommation(100.0, java.time.LocalDateTime.now(), "Eau", "Zone A");
        c1.setId(1L);
        Consommation c2 = new Consommation(200.0, java.time.LocalDateTime.now(), "Électricité", "Zone B");
        c2.setId(2L);
        Consommation c3 = new Consommation(150.0, java.time.LocalDateTime.now(), "Eau", "Zone A");
        c3.setId(3L);
        
        // Ajout au gestionnaire (type-safe)
        manager.add(c1);
        manager.add(c2);
        manager.add(c3);
        
        System.out.println("\n[1.1] Ajout de consommations au gestionnaire générique");
        System.out.println("  ✓ " + manager.count() + " consommations ajoutées");
        
        // Recherche par ID (type-safe)
        Optional<Consommation> found = OptionalUtils.ofNullable(manager.findById(2L));
        found.ifPresent(c -> 
            System.out.println("\n[1.2] Consommation trouvée par ID (2): " + c.getValeur())
        );
        
        // Filtrage avec prédicat (type-safe)
        List<Consommation> zoneA = manager.findAll(c -> "Zone A".equals(c.getZone()));
        System.out.println("\n[1.3] Consommations de Zone A: " + zoneA.size());
        
        // Le compilateur empêche d'ajouter un mauvais type
        // manager.add("Ceci ne compile pas!"); // ERREUR DE COMPILATION
        System.out.println("  ✓ Sécurité de type garantie par les génériques");
    }
    
    /**
     * EXEMPLE 2: Collections
     * Stockage volatile des données - Rapide et simple
     */
    private static void exempleCollections() {
        System.out.println("\n" + "-".repeat(70));
        System.out.println("2. COLLECTIONS - Stockage volatile");
        System.out.println("-".repeat(70));
        
        // Création de collections typées
        List<Consommation> consommations = CollectionUtils.createList();
        Set<String> zones = CollectionUtils.createSet();
        Map<String, Double> stats = CollectionUtils.createMap();
        
        // Ajout d'éléments
        Consommation c1 = new Consommation(100.0, java.time.LocalDateTime.now(), "Eau", "Zone A");
        Consommation c2 = new Consommation(200.0, java.time.LocalDateTime.now(), "Électricité", "Zone B");
        Consommation c3 = new Consommation(150.0, java.time.LocalDateTime.now(), "Eau", "Zone A");
        
        consommations.add(c1);
        consommations.add(c2);
        consommations.add(c3);
        
        System.out.println("\n[2.1] Création et manipulation de collections");
        System.out.println("  ✓ Liste de consommations: " + consommations.size() + " éléments");
        
        // Extraction des zones uniques
        zones.addAll(CollectionUtils.map(consommations, Consommation::getZone));
        System.out.println("  ✓ Zones uniques: " + zones);
        
        // Groupement par zone
        Map<String, List<Consommation>> parZone = CollectionUtils.groupBy(consommations, Consommation::getZone);
        System.out.println("\n[2.2] Groupement par zone:");
        parZone.forEach((zone, liste) -> 
            System.out.println("  - " + zone + ": " + liste.size() + " consommations")
        );
        
        // Tri
        List<Consommation> triees = CollectionUtils.sort(
            consommations,
            Comparator.comparing(Consommation::getValeur).reversed()
        );
        System.out.println("\n[2.3] Consommations triées par valeur (décroissant):");
        triees.forEach(c -> 
            System.out.println("  - " + c.getZone() + ": " + c.getValeur())
        );
    }
    
    /**
     * EXEMPLE 3: Streams & Lambdas
     * Code déclaratif, lisible et puissant pour la recherche et le traitement
     */
    private static void exempleStreamsLambdas() {
        System.out.println("\n" + "-".repeat(70));
        System.out.println("3. STREAMS & LAMBDAS - Code déclaratif");
        System.out.println("-".repeat(70));
        
        ConsommationDAO dao = new ConsommationDAO();
        List<Consommation> consommations = dao.findAll();
        
        System.out.println("\n[3.1] Filtrage avec streams et lambdas:");
        List<Consommation> grandesConsommations = consommations.stream()
                .filter(c -> c.getValeur() != null && c.getValeur() > 100)
                .collect(Collectors.toList());
        System.out.println("  ✓ Consommations > 100: " + grandesConsommations.size());
        
        System.out.println("\n[3.2] Transformation avec map:");
        List<String> zones = consommations.stream()
                .map(Consommation::getZone)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("  ✓ Zones uniques: " + zones);
        
        System.out.println("\n[3.3] Agrégation avec reduce:");
        Optional<Double> total = consommations.stream()
                .map(Consommation::getValeur)
                .filter(Objects::nonNull)
                .reduce(Double::sum);
        total.ifPresent(t -> 
            System.out.println("  ✓ Total: " + t)
        );
        
        System.out.println("\n[3.4] Groupement et statistiques:");
        Map<String, Double> sommeParZone = consommations.stream()
                .filter(c -> c.getZone() != null && c.getValeur() != null)
                .collect(Collectors.groupingBy(
                    Consommation::getZone,
                    Collectors.summingDouble(Consommation::getValeur)
                ));
        sommeParZone.forEach((zone, somme) -> 
            System.out.println("  - " + zone + ": " + somme)
        );
        
        System.out.println("\n[3.5] Recherche avec findFirst:");
        consommations.stream()
                .filter(c -> "Zone A".equals(c.getZone()))
                .findFirst()
                .ifPresent(c -> 
                    System.out.println("  ✓ Première consommation Zone A: " + c.getValeur())
                );
    }
    
    /**
     * EXEMPLE 4: Optional
     * Gestion élégante de l'absence de résultat - Évite les NullPointerException
     */
    private static void exempleOptional() {
        System.out.println("\n" + "-".repeat(70));
        System.out.println("4. OPTIONAL - Gestion de l'absence");
        System.out.println("-".repeat(70));
        
        ConsommationDAO dao = new ConsommationDAO();
        
        System.out.println("\n[4.1] Recherche avec Optional:");
        Optional<Consommation> consommation = OptionalUtils.ofNullable(dao.findById(1L));
        
        // Méthode 1: ifPresent
        consommation.ifPresent(c -> 
            System.out.println("  ✓ Consommation trouvée: " + c.getValeur())
        );
        
        // Méthode 2: orElse
        Consommation result = consommation.orElse(new Consommation(0.0, 
            java.time.LocalDateTime.now(), "Défaut", "Zone"));
        System.out.println("  ✓ Valeur ou défaut: " + result.getValeur());
        
        // Méthode 3: orElseGet (lazy)
        Consommation lazy = consommation.orElseGet(() -> {
            System.out.println("  → Calcul de la valeur par défaut...");
            return new Consommation(0.0, java.time.LocalDateTime.now(), "Défaut", "Zone");
        });
        
        System.out.println("\n[4.2] Chaînage avec map:");
        Optional<String> zone = consommation
                .map(Consommation::getZone)
                .map(String::toUpperCase);
        zone.ifPresent(z -> 
            System.out.println("  ✓ Zone en majuscules: " + z)
        );
        
        System.out.println("\n[4.3] Filtrage avec Optional:");
        consommation
                .filter(c -> c.getValeur() != null && c.getValeur() > 100)
                .ifPresent(c -> 
                    System.out.println("  ✓ Consommation > 100 trouvée: " + c.getValeur())
                );
        
        System.out.println("\n[4.4] Gestion des valeurs nulles:");
        Consommation c = null;
        Optional<Consommation> safe = OptionalUtils.safeOf(c);
        System.out.println("  ✓ Optional vide pour null: " + safe.isPresent());
        
        // Évite NullPointerException
        String zoneSafe = OptionalUtils.ofNullable(c)
                .map(Consommation::getZone)
                .orElse("Zone inconnue");
        System.out.println("  ✓ Zone sécurisée: " + zoneSafe);
    }
    
    /**
     * EXEMPLE 5: Combinaison de tous les concepts
     */
    private static void exempleCombinaison() {
        System.out.println("\n" + "-".repeat(70));
        System.out.println("5. COMBINAISON DE TOUS LES CONCEPTS");
        System.out.println("-".repeat(70));
        
        // Génériques: Gestionnaire typé
        GenericManager<Consommation> manager = new GenericManager<>(Consommation::getId);
        
        // Collections: Stockage
        ConsommationDAO dao = new ConsommationDAO();
        List<Consommation> consommations = dao.findAll();
        manager.addAll(consommations);
        
        System.out.println("\n[5.1] Utilisation combinée:");
        
        // Streams & Lambdas: Traitement déclaratif
        Map<String, Double> stats = consommations.stream()
                .filter(c -> c.getValeur() != null && c.getZone() != null)
                .collect(Collectors.groupingBy(
                    Consommation::getZone,
                    Collectors.summingDouble(Consommation::getValeur)
                ));
        
        // Optional: Gestion sécurisée
        stats.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(entry -> {
                    System.out.println("  ✓ Zone avec consommation maximale:");
                    System.out.println("    " + entry.getKey() + ": " + entry.getValue());
                });
        
        // Génériques + Streams + Optional
        Optional<Consommation> maxConsommation = manager.getAll().stream()
                .filter(c -> c.getValeur() != null)
                .max(Comparator.comparing(Consommation::getValeur));
        
        maxConsommation.ifPresent(c -> {
            System.out.println("\n[5.2] Consommation maximale trouvée:");
            System.out.println("  - Zone: " + OptionalUtils.ofNullable(c.getZone()).orElse("Inconnue"));
            System.out.println("  - Valeur: " + c.getValeur());
            System.out.println("  - Service: " + OptionalUtils.ofNullable(c.getServiceType()).orElse("Inconnu"));
        });
        
        System.out.println("\n✓ Tous les concepts Java utilisés ensemble avec succès!");
    }
}


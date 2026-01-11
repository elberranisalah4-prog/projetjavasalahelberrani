package com.municipal.dashboard.service;

import com.municipal.dashboard.model.Produit;
import com.municipal.dashboard.repository.ProduitRepository;
import com.municipal.dashboard.repository.Repository;
import com.municipal.dashboard.exception.StockException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Gestionnaire de stock avec logique métier et utilisation de Streams
 * Contient toute la logique métier pour la gestion des produits
 */
public class GestionnaireStock {
    
    private Repository<Produit, Long> repository;
    
    public GestionnaireStock() {
        this.repository = new ProduitRepository();
    }
    
    /**
     * Ajoute un produit au stock
     */
    public Produit ajouterProduit(String nom, String type, Double valeur, String zone) 
            throws StockException {
        if (nom == null || nom.trim().isEmpty()) {
            throw new StockException("STOCK_001", "Le nom du produit ne peut pas être vide");
        }
        if (valeur == null || valeur < 0) {
            throw new StockException("STOCK_002", "La valeur doit être positive");
        }
        
        Produit produit = new Produit(nom, type, valeur, LocalDateTime.now(), zone);
        return repository.save(produit);
    }
    
    /**
     * Affiche tous les produits avec Streams
     */
    public void afficherTousLesProduits() {
        System.out.println("\n[STREAMS] Liste de tous les produits:");
        System.out.println("-".repeat(70));
        
        repository.findAll().stream()
                .forEach(produit -> System.out.println("  " + produit));
        
        System.out.println("Total: " + repository.count() + " produits");
    }
    
    /**
     * Trouve un produit par ID avec Optional
     */
    public Optional<Produit> trouverProduit(Long id) {
        return repository.findById(id);
    }
    
    /**
     * Filtre les produits par type avec Streams
     */
    public List<Produit> filtrerParType(String type) {
        return repository.findAll().stream()
                .filter(p -> type.equals(p.getType()))
                .collect(Collectors.toList());
    }
    
    /**
     * Filtre les produits par zone avec Streams
     */
    public List<Produit> filtrerParZone(String zone) {
        return repository.findAll().stream()
                .filter(p -> zone.equals(p.getZone()))
                .collect(Collectors.toList());
    }
    
    /**
     * Trouve les produits avec valeur supérieure à un seuil avec Streams
     */
    public List<Produit> trouverProduitsSuperieursA(Double seuil) {
        return repository.findAll().stream()
                .filter(p -> p.getValeur() != null && p.getValeur() > seuil)
                .collect(Collectors.toList());
    }
    
    /**
     * Calcule les statistiques avec Streams
     */
    public void calculerStatistiques() {
        System.out.println("\n[STREAMS] Statistiques des produits:");
        System.out.println("-".repeat(70));
        
        List<Produit> produits = repository.findAll();
        
        if (produits.isEmpty()) {
            System.out.println("  Aucun produit dans le stock");
            return;
        }
        
        // Total
        Double total = produits.stream()
                .map(Produit::getValeur)
                .filter(Objects::nonNull)
                .reduce(0.0, Double::sum);
        System.out.println("  Total: " + total);
        
        // Moyenne
        OptionalDouble moyenne = produits.stream()
                .mapToDouble(p -> p.getValeur() != null ? p.getValeur() : 0.0)
                .average();
        moyenne.ifPresent(m -> System.out.println("  Moyenne: " + m));
        
        // Maximum
        Optional<Produit> max = produits.stream()
                .filter(p -> p.getValeur() != null)
                .max(Comparator.comparing(Produit::getValeur));
        max.ifPresent(m -> System.out.println("  Maximum: " + m.getValeur() + " (" + m.getNom() + ")"));
        
        // Minimum
        Optional<Produit> min = produits.stream()
                .filter(p -> p.getValeur() != null)
                .min(Comparator.comparing(Produit::getValeur));
        min.ifPresent(m -> System.out.println("  Minimum: " + m.getValeur() + " (" + m.getNom() + ")"));
        
        // Groupement par type
        Map<String, List<Produit>> parType = produits.stream()
                .filter(p -> p.getType() != null)
                .collect(Collectors.groupingBy(Produit::getType));
        System.out.println("\n  Groupement par type:");
        parType.forEach((type, liste) -> 
            System.out.println("    " + type + ": " + liste.size() + " produits")
        );
        
        // Groupement par zone
        Map<String, Double> sommeParZone = produits.stream()
                .filter(p -> p.getZone() != null && p.getValeur() != null)
                .collect(Collectors.groupingBy(
                    Produit::getZone,
                    Collectors.summingDouble(Produit::getValeur)
                ));
        System.out.println("\n  Somme par zone:");
        sommeParZone.forEach((zone, somme) -> 
            System.out.println("    " + zone + ": " + somme)
        );
    }
    
    /**
     * Met à jour un produit
     */
    public Produit mettreAJourProduit(Produit produit) throws StockException {
        if (produit.getId() == null) {
            throw new StockException("STOCK_003", "L'ID du produit est requis pour la mise à jour");
        }
        if (!repository.existsById(produit.getId())) {
            throw new StockException("STOCK_004", "Produit non trouvé avec l'ID: " + produit.getId());
        }
        
        return repository.update(produit);
    }
    
    /**
     * Supprime un produit
     */
    public boolean supprimerProduit(Long id) throws StockException {
        if (!repository.existsById(id)) {
            throw new StockException("STOCK_005", "Produit non trouvé avec l'ID: " + id);
        }
        
        return repository.deleteById(id);
    }
    
    /**
     * Trouve les top N produits par valeur avec Streams
     */
    public List<Produit> trouverTopProduits(int n) {
        return repository.findAll().stream()
                .filter(p -> p.getValeur() != null)
                .sorted(Comparator.comparing(Produit::getValeur).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }
    
    /**
     * Compte les produits par type avec Streams
     */
    public Map<String, Long> compterParType() {
        return repository.findAll().stream()
                .filter(p -> p.getType() != null)
                .collect(Collectors.groupingBy(
                    Produit::getType,
                    Collectors.counting()
                ));
    }
}


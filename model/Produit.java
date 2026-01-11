package com.municipal.dashboard.model;

import java.time.LocalDateTime;

/**
 * Modèle de données pour un produit/service municipal
 * Représente une entité métier du système
 */
public class Produit {
    private Long id;
    private String nom;
    private String type;
    private Double valeur;
    private LocalDateTime date;
    private String zone;
    private String description;
    
    public Produit() {
    }
    
    public Produit(String nom, String type, Double valeur, LocalDateTime date, String zone) {
        this.nom = nom;
        this.type = type;
        this.valeur = valeur;
        this.date = date;
        this.zone = zone;
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Double getValeur() {
        return valeur;
    }
    
    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }
    
    public LocalDateTime getDate() {
        return date;
    }
    
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    
    public String getZone() {
        return zone;
    }
    
    public void setZone(String zone) {
        this.zone = zone;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return String.format("Produit{id=%d, nom='%s', type='%s', valeur=%.2f, zone='%s', date=%s}",
                id, nom, type, valeur, zone, date);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produit produit = (Produit) o;
        return id != null && id.equals(produit.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}


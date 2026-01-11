package com.municipal.dashboard;

import java.time.LocalDateTime;

public class Indicator {
    private Long id;
    private String nom;
    private String type;
    private Double valeur;
    private Double valeurCible;
    private LocalDateTime dateCalcul;
    private String unite;
    private String description;
    private String statut;

    public Indicator() {
    }

    public Indicator(String nom, String type, Double valeur, LocalDateTime dateCalcul, String unite) {
        this.nom = nom;
        this.type = type;
        this.valeur = valeur;
        this.dateCalcul = dateCalcul;
        this.unite = unite;
    }

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

    public Double getValeurCible() {
        return valeurCible;
    }

    public void setValeurCible(Double valeurCible) {
        this.valeurCible = valeurCible;
    }

    public LocalDateTime getDateCalcul() {
        return dateCalcul;
    }

    public void setDateCalcul(LocalDateTime dateCalcul) {
        this.dateCalcul = dateCalcul;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "Indicator{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", type='" + type + '\'' +
                ", valeur=" + valeur +
                ", valeurCible=" + valeurCible +
                ", statut='" + statut + '\'' +
                '}';
    }
}


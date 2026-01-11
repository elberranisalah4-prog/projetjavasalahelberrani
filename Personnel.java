package com.municipal.dashboard;

import java.time.LocalDateTime;

public class Personnel {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String poste;
    private String departement;
    private LocalDateTime dateEmbauche;
    private String statut;
    private String matricule;

    public Personnel() {
    }

    public Personnel(String nom, String prenom, String email, String telephone, 
                    String poste, String departement, LocalDateTime dateEmbauche, String statut) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.poste = poste;
        this.departement = departement;
        this.dateEmbauche = dateEmbauche;
        this.statut = statut;
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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public LocalDateTime getDateEmbauche() {
        return dateEmbauche;
    }

    public void setDateEmbauche(LocalDateTime dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    @Override
    public String toString() {
        return "Personnel{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", poste='" + poste + '\'' +
                ", departement='" + departement + '\'' +
                ", statut='" + statut + '\'' +
                '}';
    }
}

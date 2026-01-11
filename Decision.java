package com.municipal.dashboard;

import java.time.LocalDateTime;

public class Decision {
    private Long id;
    private String titre;
    private String description;
    private String type;
    private String statut;
    private LocalDateTime dateDecision;
    private LocalDateTime dateApplication;
    private String auteur;
    private String serviceConcerne;
    private String zoneConcernee;
    private String justification;

    public Decision() {
    }

    public Decision(String titre, String description, String type, String statut, 
                   LocalDateTime dateDecision, String auteur, String serviceConcerne) {
        this.titre = titre;
        this.description = description;
        this.type = type;
        this.statut = statut;
        this.dateDecision = dateDecision;
        this.auteur = auteur;
        this.serviceConcerne = serviceConcerne;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateDecision() {
        return dateDecision;
    }

    public void setDateDecision(LocalDateTime dateDecision) {
        this.dateDecision = dateDecision;
    }

    public LocalDateTime getDateApplication() {
        return dateApplication;
    }

    public void setDateApplication(LocalDateTime dateApplication) {
        this.dateApplication = dateApplication;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getServiceConcerne() {
        return serviceConcerne;
    }

    public void setServiceConcerne(String serviceConcerne) {
        this.serviceConcerne = serviceConcerne;
    }

    public String getZoneConcernee() {
        return zoneConcernee;
    }

    public void setZoneConcernee(String zoneConcernee) {
        this.zoneConcernee = zoneConcernee;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    @Override
    public String toString() {
        return "Decision{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", type='" + type + '\'' +
                ", statut='" + statut + '\'' +
                ", dateDecision=" + dateDecision +
                ", auteur='" + auteur + '\'' +
                '}';
    }
}


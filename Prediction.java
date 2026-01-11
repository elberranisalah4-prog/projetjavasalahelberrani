package com.municipal.dashboard;

import java.time.LocalDateTime;

public class Prediction {
    private Long id;
    private String serviceType;
    private LocalDateTime datePrediction;
    private Double valeurPredite;
    private Double confiance;
    private String zone;
    private String typePrediction;
    private String recommandation;
    private LocalDateTime dateCalcul;

    public Prediction() {
    }

    public Prediction(String serviceType, LocalDateTime datePrediction, Double valeurPredite, 
                     Double confiance, String zone, String typePrediction) {
        this.serviceType = serviceType;
        this.datePrediction = datePrediction;
        this.valeurPredite = valeurPredite;
        this.confiance = confiance;
        this.zone = zone;
        this.typePrediction = typePrediction;
        this.dateCalcul = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public LocalDateTime getDatePrediction() {
        return datePrediction;
    }

    public void setDatePrediction(LocalDateTime datePrediction) {
        this.datePrediction = datePrediction;
    }

    public Double getValeurPredite() {
        return valeurPredite;
    }

    public void setValeurPredite(Double valeurPredite) {
        this.valeurPredite = valeurPredite;
    }

    public Double getConfiance() {
        return confiance;
    }

    public void setConfiance(Double confiance) {
        this.confiance = confiance;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getTypePrediction() {
        return typePrediction;
    }

    public void setTypePrediction(String typePrediction) {
        this.typePrediction = typePrediction;
    }

    public String getRecommandation() {
        return recommandation;
    }

    public void setRecommandation(String recommandation) {
        this.recommandation = recommandation;
    }

    public LocalDateTime getDateCalcul() {
        return dateCalcul;
    }

    public void setDateCalcul(LocalDateTime dateCalcul) {
        this.dateCalcul = dateCalcul;
    }

    @Override
    public String toString() {
        return "Prediction{" +
                "id=" + id +
                ", serviceType='" + serviceType + '\'' +
                ", datePrediction=" + datePrediction +
                ", valeurPredite=" + valeurPredite +
                ", confiance=" + confiance +
                ", zone='" + zone + '\'' +
                '}';
    }
}


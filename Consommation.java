package com.municipal.dashboard;

import java.time.LocalDateTime;

public class Consommation {
    private Long id;
    private Double valeur;
    private LocalDateTime date;
    private String serviceType;
    private String zone;

    public Consommation() {
    }

    public Consommation(Double valeur, LocalDateTime date, String serviceType, String zone) {
        this.valeur = valeur;
        this.date = date;
        this.serviceType = serviceType;
        this.zone = zone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "Consommation{" +
                "id=" + id +
                ", valeur=" + valeur +
                ", date=" + date +
                ", serviceType='" + serviceType + '\'' +
                ", zone='" + zone + '\'' +
                '}';
    }
}


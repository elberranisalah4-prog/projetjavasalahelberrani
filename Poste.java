package com.municipal.dashboard;

public class Poste {
    private Long id;
    private String titre;
    private String type;
    private String description;
    private String departement;
    private String niveau;
    private Double salaireMin;
    private Double salaireMax;

    public Poste() {
    }

    public Poste(String titre, String type, String description, String departement, 
                 String niveau, Double salaireMin, Double salaireMax) {
        this.titre = titre;
        this.type = type;
        this.description = description;
        this.departement = departement;
        this.niveau = niveau;
        this.salaireMin = salaireMin;
        this.salaireMax = salaireMax;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public Double getSalaireMin() {
        return salaireMin;
    }

    public void setSalaireMin(Double salaireMin) {
        this.salaireMin = salaireMin;
    }

    public Double getSalaireMax() {
        return salaireMax;
    }

    public void setSalaireMax(Double salaireMax) {
        this.salaireMax = salaireMax;
    }

    @Override
    public String toString() {
        return "Poste{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", departement='" + departement + '\'' +
                ", niveau='" + niveau + '\'' +
                ", salaireMin=" + salaireMin +
                ", salaireMax=" + salaireMax +
                '}';
    }
}


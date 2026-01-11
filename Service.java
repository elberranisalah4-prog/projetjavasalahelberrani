package com.municipal.dashboard;

public class Service {
    private Long id;
    private String type;
    private String nom;
    private String description;

    public Service() {
    }

    public Service(String type, String nom, String description) {
        this.type = type;
        this.nom = nom;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}


package com.municipal.dashboard;

import java.time.LocalDateTime;

public class Note {
    private Long id;
    private String contenu;
    private LocalDateTime dateCreation;
    private String auteur;
    private String type;

    public Note() {
    }

    public Note(String contenu, LocalDateTime dateCreation, String auteur, String type) {
        this.contenu = contenu;
        this.dateCreation = dateCreation;
        this.auteur = auteur;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", contenu='" + contenu + '\'' +
                ", dateCreation=" + dateCreation +
                ", auteur='" + auteur + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}


package com.municipal.dashboard.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class DashboardView {
    private VBox view;
    
    public DashboardView() {
        createView();
    }
    
    private void createView() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        view.setAlignment(Pos.TOP_CENTER);
        
        Label title = new Label("Tableau de Bord");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        // Grille de statistiques
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(20);
        statsGrid.setVgap(20);
        statsGrid.setAlignment(Pos.CENTER);
        
        // Cartes de statistiques
        statsGrid.add(createStatCard("Services", "12", "#3498db"), 0, 0);
        statsGrid.add(createStatCard("Zones", "8", "#2ecc71"), 1, 0);
        statsGrid.add(createStatCard("Personnel", "45", "#e74c3c"), 2, 0);
        statsGrid.add(createStatCard("Indicateurs", "24", "#f39c12"), 3, 0);
        
        Label welcome = new Label("Bienvenue dans le Dashboard Services Publics");
        welcome.setStyle("-fx-font-size: 18px; -fx-text-fill: #7f8c8d;");
        
        view.getChildren().addAll(title, welcome, statsGrid);
    }
    
    private VBox createStatCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setMinWidth(200);
        card.setMinHeight(150);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        card.setAlignment(Pos.CENTER);
        
        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");
        
        card.getChildren().addAll(valueLabel, titleLabel);
        return card;
    }
    
    public VBox getView() {
        return view;
    }
}


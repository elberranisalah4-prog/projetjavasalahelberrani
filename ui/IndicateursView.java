package com.municipal.dashboard.ui;

import com.municipal.dashboard.Indicator;
import com.municipal.dashboard.dao.IndicatorDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import java.time.LocalDateTime;
import java.util.List;

public class IndicateursView {
    private VBox view;
    private TableView<Indicator> table;
    private ObservableList<Indicator> indicateursList;
    private IndicatorDAO indicatorDAO;
    
    public IndicateursView() {
        this.indicatorDAO = new IndicatorDAO();
        this.indicateursList = FXCollections.observableArrayList();
        createView();
        loadData();
    }
    
    private void createView() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        
        Label title = new Label("Gestion des Indicateurs");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        
        Button btnAdd = new Button("➕ Ajouter");
        Button btnEdit = new Button("✏️ Modifier");
        Button btnDelete = new Button("🗑️ Supprimer");
        Button btnRefresh = new Button("🔄 Actualiser");
        
        btnAdd.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-padding: 10 20;");
        btnEdit.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 10 20;");
        btnDelete.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 10 20;");
        btnRefresh.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-padding: 10 20;");
        
        btnAdd.setOnAction(e -> showAddDialog());
        btnEdit.setOnAction(e -> showEditDialog());
        btnDelete.setOnAction(e -> deleteSelected());
        btnRefresh.setOnAction(e -> loadData());
        
        toolbar.getChildren().addAll(btnAdd, btnEdit, btnDelete, btnRefresh);
        
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<Indicator, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<Indicator, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        
        TableColumn<Indicator, Double> valeurCol = new TableColumn<>("Valeur");
        valeurCol.setCellValueFactory(new PropertyValueFactory<>("valeur"));
        
        TableColumn<Indicator, Double> cibleCol = new TableColumn<>("Valeur Cible");
        cibleCol.setCellValueFactory(new PropertyValueFactory<>("valeurCible"));
        
        TableColumn<Indicator, String> statutCol = new TableColumn<>("Statut");
        statutCol.setCellValueFactory(new PropertyValueFactory<>("statut"));
        
        table.getColumns().addAll(idCol, nomCol, valeurCol, cibleCol, statutCol);
        table.setItems(indicateursList);
        
        VBox.setVgrow(table, Priority.ALWAYS);
        view.getChildren().addAll(title, toolbar, table);
    }
    
    private void loadData() {
        indicateursList.clear();
        indicateursList.addAll(indicatorDAO.findAll());
    }
    
    private void showAddDialog() {
        Dialog<Indicator> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un Indicateur");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nomField = new TextField();
        TextField typeField = new TextField();
        TextField valeurField = new TextField();
        TextField valeurCibleField = new TextField();
        TextField uniteField = new TextField();
        TextField statutField = new TextField();
        
        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Type:"), 0, 1);
        grid.add(typeField, 1, 1);
        grid.add(new Label("Valeur:"), 0, 2);
        grid.add(valeurField, 1, 2);
        grid.add(new Label("Valeur Cible:"), 0, 3);
        grid.add(valeurCibleField, 1, 3);
        grid.add(new Label("Unité:"), 0, 4);
        grid.add(uniteField, 1, 4);
        grid.add(new Label("Statut:"), 0, 5);
        grid.add(statutField, 1, 5);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE), ButtonType.CANCEL);
        
        dialog.setResultConverter(btn -> {
            if (btn.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                try {
                    Indicator indicator = new Indicator(
                        nomField.getText(), typeField.getText(),
                        Double.parseDouble(valeurField.getText()),
                        LocalDateTime.now(), uniteField.getText()
                    );
                    indicator.setValeurCible(Double.parseDouble(valeurCibleField.getText()));
                    indicator.setStatut(statutField.getText());
                    indicatorDAO.insert(indicator);
                    loadData();
                    return indicator;
                } catch (NumberFormatException e) {
                    showAlert("Erreur", "Veuillez entrer des valeurs numériques valides.");
                }
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private void showEditDialog() {
        Indicator selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner un indicateur à modifier.");
            return;
        }
        
        Dialog<Indicator> dialog = new Dialog<>();
        dialog.setTitle("Modifier un Indicateur");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField valeurField = new TextField(String.valueOf(selected.getValeur()));
        TextField valeurCibleField = new TextField(String.valueOf(selected.getValeurCible()));
        
        grid.add(new Label("Valeur:"), 0, 0);
        grid.add(valeurField, 1, 0);
        grid.add(new Label("Valeur Cible:"), 0, 1);
        grid.add(valeurCibleField, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE), ButtonType.CANCEL);
        
        dialog.setResultConverter(btn -> {
            if (btn.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                try {
                    selected.setValeur(Double.parseDouble(valeurField.getText()));
                    selected.setValeurCible(Double.parseDouble(valeurCibleField.getText()));
                    indicatorDAO.update(selected);
                    loadData();
                    return selected;
                } catch (NumberFormatException e) {
                    showAlert("Erreur", "Veuillez entrer des valeurs numériques valides.");
                }
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private void deleteSelected() {
        Indicator selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner un indicateur à supprimer.");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer l'indicateur");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cet indicateur ?");
        
        if (alert.showAndWait().get() == ButtonType.OK) {
            indicatorDAO.delete(selected.getId());
            loadData();
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public VBox getView() {
        return view;
    }
}


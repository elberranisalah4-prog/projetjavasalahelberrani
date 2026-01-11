package com.municipal.dashboard.ui;

import com.municipal.dashboard.Consommation;
import com.municipal.dashboard.dao.ConsommationDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import java.time.LocalDateTime;
import java.util.List;

public class ConsommationsView {
    private VBox view;
    private TableView<Consommation> table;
    private ObservableList<Consommation> consommationsList;
    private ConsommationDAO consommationDAO;
    
    public ConsommationsView() {
        this.consommationDAO = new ConsommationDAO();
        this.consommationsList = FXCollections.observableArrayList();
        createView();
        loadData();
    }
    
    private void createView() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        
        Label title = new Label("Gestion des Consommations");
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
        
        TableColumn<Consommation, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<Consommation, Double> valeurCol = new TableColumn<>("Valeur");
        valeurCol.setCellValueFactory(new PropertyValueFactory<>("valeur"));
        
        TableColumn<Consommation, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        TableColumn<Consommation, String> serviceCol = new TableColumn<>("Service");
        serviceCol.setCellValueFactory(new PropertyValueFactory<>("serviceType"));
        
        TableColumn<Consommation, String> zoneCol = new TableColumn<>("Zone");
        zoneCol.setCellValueFactory(new PropertyValueFactory<>("zone"));
        
        table.getColumns().addAll(idCol, valeurCol, dateCol, serviceCol, zoneCol);
        table.setItems(consommationsList);
        
        VBox.setVgrow(table, Priority.ALWAYS);
        view.getChildren().addAll(title, toolbar, table);
    }
    
    private void loadData() {
        consommationsList.clear();
        consommationsList.addAll(consommationDAO.findAll());
    }
    
    private void showAddDialog() {
        Dialog<Consommation> dialog = new Dialog<>();
        dialog.setTitle("Ajouter une Consommation");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField valeurField = new TextField();
        TextField serviceField = new TextField();
        TextField zoneField = new TextField();
        
        grid.add(new Label("Valeur:"), 0, 0);
        grid.add(valeurField, 1, 0);
        grid.add(new Label("Type de service:"), 0, 1);
        grid.add(serviceField, 1, 1);
        grid.add(new Label("Zone:"), 0, 2);
        grid.add(zoneField, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE), ButtonType.CANCEL);
        
        dialog.setResultConverter(btn -> {
            if (btn.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                try {
                    Consommation consommation = new Consommation(
                        Double.parseDouble(valeurField.getText()),
                        LocalDateTime.now(),
                        serviceField.getText(),
                        zoneField.getText()
                    );
                    consommationDAO.insert(consommation);
                    loadData();
                    return consommation;
                } catch (NumberFormatException e) {
                    showAlert("Erreur", "Veuillez entrer une valeur numérique valide.");
                }
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private void showEditDialog() {
        Consommation selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner une consommation à modifier.");
            return;
        }
        
        Dialog<Consommation> dialog = new Dialog<>();
        dialog.setTitle("Modifier une Consommation");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField valeurField = new TextField(String.valueOf(selected.getValeur()));
        
        grid.add(new Label("Valeur:"), 0, 0);
        grid.add(valeurField, 1, 0);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE), ButtonType.CANCEL);
        
        dialog.setResultConverter(btn -> {
            if (btn.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                try {
                    selected.setValeur(Double.parseDouble(valeurField.getText()));
                    consommationDAO.update(selected);
                    loadData();
                    return selected;
                } catch (NumberFormatException e) {
                    showAlert("Erreur", "Veuillez entrer une valeur numérique valide.");
                }
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private void deleteSelected() {
        Consommation selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner une consommation à supprimer.");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer la consommation");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette consommation ?");
        
        if (alert.showAndWait().get() == ButtonType.OK) {
            consommationDAO.delete(selected.getId());
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


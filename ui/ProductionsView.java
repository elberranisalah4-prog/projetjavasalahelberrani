package com.municipal.dashboard.ui;

import com.municipal.dashboard.Production;
import com.municipal.dashboard.dao.ProductionDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import java.time.LocalDateTime;
import java.util.List;

public class ProductionsView {
    private VBox view;
    private TableView<Production> table;
    private ObservableList<Production> productionsList;
    private ProductionDAO productionDAO;
    
    public ProductionsView() {
        this.productionDAO = new ProductionDAO();
        this.productionsList = FXCollections.observableArrayList();
        createView();
        loadData();
    }
    
    private void createView() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        
        Label title = new Label("Gestion des Productions");
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
        
        TableColumn<Production, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<Production, Double> valeurCol = new TableColumn<>("Valeur");
        valeurCol.setCellValueFactory(new PropertyValueFactory<>("valeur"));
        
        TableColumn<Production, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        TableColumn<Production, String> serviceCol = new TableColumn<>("Service");
        serviceCol.setCellValueFactory(new PropertyValueFactory<>("serviceType"));
        
        TableColumn<Production, String> zoneCol = new TableColumn<>("Zone");
        zoneCol.setCellValueFactory(new PropertyValueFactory<>("zone"));
        
        table.getColumns().addAll(idCol, valeurCol, dateCol, serviceCol, zoneCol);
        table.setItems(productionsList);
        
        VBox.setVgrow(table, Priority.ALWAYS);
        view.getChildren().addAll(title, toolbar, table);
    }
    
    private void loadData() {
        productionsList.clear();
        productionsList.addAll(productionDAO.findAll());
    }
    
    private void showAddDialog() {
        Dialog<Production> dialog = new Dialog<>();
        dialog.setTitle("Ajouter une Production");
        
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
                    Production production = new Production(
                        Double.parseDouble(valeurField.getText()),
                        LocalDateTime.now(),
                        serviceField.getText(),
                        zoneField.getText()
                    );
                    productionDAO.insert(production);
                    loadData();
                    return production;
                } catch (NumberFormatException e) {
                    showAlert("Erreur", "Veuillez entrer une valeur numérique valide.");
                }
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private void showEditDialog() {
        Production selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner une production à modifier.");
            return;
        }
        
        Dialog<Production> dialog = new Dialog<>();
        dialog.setTitle("Modifier une Production");
        
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
                    productionDAO.update(selected);
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
        Production selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner une production à supprimer.");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer la production");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette production ?");
        
        if (alert.showAndWait().get() == ButtonType.OK) {
            productionDAO.delete(selected.getId());
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


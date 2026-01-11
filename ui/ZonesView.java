package com.municipal.dashboard.ui;

import com.municipal.dashboard.Zone;
import com.municipal.dashboard.dao.ZoneDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import java.util.List;

public class ZonesView {
    private VBox view;
    private TableView<Zone> table;
    private ObservableList<Zone> zonesList;
    private ZoneDAO zoneDAO;
    
    public ZonesView() {
        this.zoneDAO = new ZoneDAO();
        this.zonesList = FXCollections.observableArrayList();
        createView();
        loadData();
    }
    
    private void createView() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        
        Label title = new Label("Gestion des Zones");
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
        
        TableColumn<Zone, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<Zone, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        
        TableColumn<Zone, String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        
        TableColumn<Zone, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        
        table.getColumns().addAll(idCol, nomCol, codeCol, descCol);
        table.setItems(zonesList);
        
        VBox.setVgrow(table, Priority.ALWAYS);
        view.getChildren().addAll(title, toolbar, table);
    }
    
    private void loadData() {
        zonesList.clear();
        zonesList.addAll(zoneDAO.findAll());
    }
    
    private void showAddDialog() {
        Dialog<Zone> dialog = new Dialog<>();
        dialog.setTitle("Ajouter une Zone");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nomField = new TextField();
        TextField codeField = new TextField();
        TextArea descField = new TextArea();
        descField.setPrefRowCount(3);
        
        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Code:"), 0, 1);
        grid.add(codeField, 1, 1);
        grid.add(new Label("Description:"), 0, 2);
        grid.add(descField, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE), ButtonType.CANCEL);
        
        dialog.setResultConverter(btn -> {
            if (btn.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                Zone zone = new Zone(nomField.getText(), codeField.getText(), descField.getText());
                zoneDAO.insert(zone);
                loadData();
                return zone;
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private void showEditDialog() {
        Zone selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner une zone à modifier.");
            return;
        }
        
        Dialog<Zone> dialog = new Dialog<>();
        dialog.setTitle("Modifier une Zone");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nomField = new TextField(selected.getNom());
        TextField codeField = new TextField(selected.getCode());
        TextArea descField = new TextArea(selected.getDescription());
        descField.setPrefRowCount(3);
        
        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Code:"), 0, 1);
        grid.add(codeField, 1, 1);
        grid.add(new Label("Description:"), 0, 2);
        grid.add(descField, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE), ButtonType.CANCEL);
        
        dialog.setResultConverter(btn -> {
            if (btn.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                selected.setNom(nomField.getText());
                selected.setCode(codeField.getText());
                selected.setDescription(descField.getText());
                zoneDAO.update(selected);
                loadData();
                return selected;
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private void deleteSelected() {
        Zone selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner une zone à supprimer.");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer la zone");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette zone ?");
        
        if (alert.showAndWait().get() == ButtonType.OK) {
            zoneDAO.delete(selected.getId());
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


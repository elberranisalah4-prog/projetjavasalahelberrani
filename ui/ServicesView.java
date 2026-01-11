package com.municipal.dashboard.ui;

import com.municipal.dashboard.Service;
import com.municipal.dashboard.dao.ServiceDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import java.util.List;

public class ServicesView {
    private VBox view;
    private TableView<Service> table;
    private ObservableList<Service> servicesList;
    private ServiceDAO serviceDAO;
    
    public ServicesView() {
        this.serviceDAO = new ServiceDAO();
        this.servicesList = FXCollections.observableArrayList();
        createView();
        loadData();
    }
    
    private void createView() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        
        // Titre
        Label title = new Label("Gestion des Services");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        // Barre d'outils
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
        
        // Table
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<Service, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(80);
        
        TableColumn<Service, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setPrefWidth(150);
        
        TableColumn<Service, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        nomCol.setPrefWidth(200);
        
        TableColumn<Service, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descCol.setPrefWidth(400);
        
        table.getColumns().addAll(idCol, typeCol, nomCol, descCol);
        table.setItems(servicesList);
        
        VBox.setVgrow(table, Priority.ALWAYS);
        
        view.getChildren().addAll(title, toolbar, table);
    }
    
    private void loadData() {
        servicesList.clear();
        List<Service> services = serviceDAO.findAll();
        servicesList.addAll(services);
    }
    
    private void showAddDialog() {
        Dialog<Service> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un Service");
        dialog.setHeaderText("Nouveau Service");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField typeField = new TextField();
        TextField nomField = new TextField();
        TextArea descField = new TextArea();
        descField.setPrefRowCount(3);
        
        grid.add(new Label("Type:"), 0, 0);
        grid.add(typeField, 1, 0);
        grid.add(new Label("Nom:"), 0, 1);
        grid.add(nomField, 1, 1);
        grid.add(new Label("Description:"), 0, 2);
        grid.add(descField, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        
        ButtonType addButton = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                Service service = new Service(
                    typeField.getText(),
                    nomField.getText(),
                    descField.getText()
                );
                serviceDAO.insert(service);
                loadData();
                return service;
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private void showEditDialog() {
        Service selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner un service à modifier.");
            return;
        }
        
        Dialog<Service> dialog = new Dialog<>();
        dialog.setTitle("Modifier un Service");
        dialog.setHeaderText("Modifier le Service");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField typeField = new TextField(selected.getType());
        TextField nomField = new TextField(selected.getNom());
        TextArea descField = new TextArea(selected.getDescription());
        descField.setPrefRowCount(3);
        
        grid.add(new Label("Type:"), 0, 0);
        grid.add(typeField, 1, 0);
        grid.add(new Label("Nom:"), 0, 1);
        grid.add(nomField, 1, 1);
        grid.add(new Label("Description:"), 0, 2);
        grid.add(descField, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        
        ButtonType saveButton = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButton) {
                selected.setType(typeField.getText());
                selected.setNom(nomField.getText());
                selected.setDescription(descField.getText());
                serviceDAO.update(selected);
                loadData();
                return selected;
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private void deleteSelected() {
        Service selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner un service à supprimer.");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer le service");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce service ?");
        
        if (alert.showAndWait().get() == ButtonType.OK) {
            serviceDAO.delete(selected.getId());
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


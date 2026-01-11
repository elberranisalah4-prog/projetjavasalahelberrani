package com.municipal.dashboard.ui;

import com.municipal.dashboard.Decision;
import com.municipal.dashboard.dao.DecisionDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import java.time.LocalDateTime;
import java.util.List;

public class DecisionsView {
    private VBox view;
    private TableView<Decision> table;
    private ObservableList<Decision> decisionsList;
    private DecisionDAO decisionDAO;
    
    public DecisionsView() {
        this.decisionDAO = new DecisionDAO();
        this.decisionsList = FXCollections.observableArrayList();
        createView();
        loadData();
    }
    
    private void createView() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        
        Label title = new Label("Gestion des Décisions");
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
        
        TableColumn<Decision, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<Decision, String> titreCol = new TableColumn<>("Titre");
        titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        
        TableColumn<Decision, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        
        TableColumn<Decision, String> statutCol = new TableColumn<>("Statut");
        statutCol.setCellValueFactory(new PropertyValueFactory<>("statut"));
        
        TableColumn<Decision, String> auteurCol = new TableColumn<>("Auteur");
        auteurCol.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        
        table.getColumns().addAll(idCol, titreCol, typeCol, statutCol, auteurCol);
        table.setItems(decisionsList);
        
        VBox.setVgrow(table, Priority.ALWAYS);
        view.getChildren().addAll(title, toolbar, table);
    }
    
    private void loadData() {
        decisionsList.clear();
        decisionsList.addAll(decisionDAO.findAll());
    }
    
    private void showAddDialog() {
        Dialog<Decision> dialog = new Dialog<>();
        dialog.setTitle("Ajouter une Décision");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField titreField = new TextField();
        TextArea descField = new TextArea();
        descField.setPrefRowCount(3);
        TextField typeField = new TextField();
        TextField statutField = new TextField();
        TextField auteurField = new TextField();
        
        grid.add(new Label("Titre:"), 0, 0);
        grid.add(titreField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descField, 1, 1);
        grid.add(new Label("Type:"), 0, 2);
        grid.add(typeField, 1, 2);
        grid.add(new Label("Statut:"), 0, 3);
        grid.add(statutField, 1, 3);
        grid.add(new Label("Auteur:"), 0, 4);
        grid.add(auteurField, 1, 4);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE), ButtonType.CANCEL);
        
        dialog.setResultConverter(btn -> {
            if (btn.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                Decision decision = new Decision(
                    titreField.getText(), descField.getText(), typeField.getText(),
                    statutField.getText(), LocalDateTime.now(), auteurField.getText(), ""
                );
                decisionDAO.insert(decision);
                loadData();
                return decision;
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private void showEditDialog() {
        Decision selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner une décision à modifier.");
            return;
        }
        
        Dialog<Decision> dialog = new Dialog<>();
        dialog.setTitle("Modifier une Décision");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField statutField = new TextField(selected.getStatut());
        
        grid.add(new Label("Statut:"), 0, 0);
        grid.add(statutField, 1, 0);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE), ButtonType.CANCEL);
        
        dialog.setResultConverter(btn -> {
            if (btn.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                selected.setStatut(statutField.getText());
                decisionDAO.update(selected);
                loadData();
                return selected;
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private void deleteSelected() {
        Decision selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner une décision à supprimer.");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer la décision");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette décision ?");
        
        if (alert.showAndWait().get() == ButtonType.OK) {
            decisionDAO.delete(selected.getId());
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


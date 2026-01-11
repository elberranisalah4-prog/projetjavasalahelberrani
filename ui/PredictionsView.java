package com.municipal.dashboard.ui;

import com.municipal.dashboard.Prediction;
import com.municipal.dashboard.dao.PredictionDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import java.time.LocalDateTime;
import java.util.List;

public class PredictionsView {
    private VBox view;
    private TableView<Prediction> table;
    private ObservableList<Prediction> predictionsList;
    private PredictionDAO predictionDAO;
    
    public PredictionsView() {
        this.predictionDAO = new PredictionDAO();
        this.predictionsList = FXCollections.observableArrayList();
        createView();
        loadData();
    }
    
    private void createView() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        
        Label title = new Label("Gestion des Prédictions");
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
        
        TableColumn<Prediction, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<Prediction, String> serviceCol = new TableColumn<>("Service");
        serviceCol.setCellValueFactory(new PropertyValueFactory<>("serviceType"));
        
        TableColumn<Prediction, Double> valeurCol = new TableColumn<>("Valeur Prédite");
        valeurCol.setCellValueFactory(new PropertyValueFactory<>("valeurPredite"));
        
        TableColumn<Prediction, Double> confianceCol = new TableColumn<>("Confiance (%)");
        confianceCol.setCellValueFactory(new PropertyValueFactory<>("confiance"));
        
        TableColumn<Prediction, String> zoneCol = new TableColumn<>("Zone");
        zoneCol.setCellValueFactory(new PropertyValueFactory<>("zone"));
        
        table.getColumns().addAll(idCol, serviceCol, valeurCol, confianceCol, zoneCol);
        table.setItems(predictionsList);
        
        VBox.setVgrow(table, Priority.ALWAYS);
        view.getChildren().addAll(title, toolbar, table);
    }
    
    private void loadData() {
        predictionsList.clear();
        predictionsList.addAll(predictionDAO.findAll());
    }
    
    private void showAddDialog() {
        Dialog<Prediction> dialog = new Dialog<>();
        dialog.setTitle("Ajouter une Prédiction");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField serviceField = new TextField();
        TextField valeurField = new TextField();
        TextField confianceField = new TextField();
        TextField zoneField = new TextField();
        TextField typeField = new TextField();
        
        grid.add(new Label("Type de service:"), 0, 0);
        grid.add(serviceField, 1, 0);
        grid.add(new Label("Valeur prédite:"), 0, 1);
        grid.add(valeurField, 1, 1);
        grid.add(new Label("Confiance (%):"), 0, 2);
        grid.add(confianceField, 1, 2);
        grid.add(new Label("Zone:"), 0, 3);
        grid.add(zoneField, 1, 3);
        grid.add(new Label("Type de prédiction:"), 0, 4);
        grid.add(typeField, 1, 4);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE), ButtonType.CANCEL);
        
        dialog.setResultConverter(btn -> {
            if (btn.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                try {
                    Prediction prediction = new Prediction(
                        serviceField.getText(),
                        LocalDateTime.now().plusMonths(1),
                        Double.parseDouble(valeurField.getText()),
                        Double.parseDouble(confianceField.getText()),
                        zoneField.getText(),
                        typeField.getText()
                    );
                    predictionDAO.insert(prediction);
                    loadData();
                    return prediction;
                } catch (NumberFormatException e) {
                    showAlert("Erreur", "Veuillez entrer des valeurs numériques valides.");
                }
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private void showEditDialog() {
        Prediction selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner une prédiction à modifier.");
            return;
        }
        
        Dialog<Prediction> dialog = new Dialog<>();
        dialog.setTitle("Modifier une Prédiction");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField valeurField = new TextField(String.valueOf(selected.getValeurPredite()));
        TextField confianceField = new TextField(String.valueOf(selected.getConfiance()));
        
        grid.add(new Label("Valeur prédite:"), 0, 0);
        grid.add(valeurField, 1, 0);
        grid.add(new Label("Confiance (%):"), 0, 1);
        grid.add(confianceField, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE), ButtonType.CANCEL);
        
        dialog.setResultConverter(btn -> {
            if (btn.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                try {
                    selected.setValeurPredite(Double.parseDouble(valeurField.getText()));
                    selected.setConfiance(Double.parseDouble(confianceField.getText()));
                    predictionDAO.update(selected);
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
        Prediction selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner une prédiction à supprimer.");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer la prédiction");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette prédiction ?");
        
        if (alert.showAndWait().get() == ButtonType.OK) {
            predictionDAO.delete(selected.getId());
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


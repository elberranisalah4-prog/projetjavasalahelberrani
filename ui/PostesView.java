package com.municipal.dashboard.ui;

import com.municipal.dashboard.Poste;
import com.municipal.dashboard.dao.PosteDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import java.util.List;

public class PostesView {
    private VBox view;
    private TableView<Poste> table;
    private ObservableList<Poste> postesList;
    private PosteDAO posteDAO;
    
    public PostesView() {
        this.posteDAO = new PosteDAO();
        this.postesList = FXCollections.observableArrayList();
        createView();
        loadData();
    }
    
    private void createView() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        
        Label title = new Label("Gestion des Postes");
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
        
        TableColumn<Poste, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<Poste, String> titreCol = new TableColumn<>("Titre");
        titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        
        TableColumn<Poste, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        
        TableColumn<Poste, String> deptCol = new TableColumn<>("Département");
        deptCol.setCellValueFactory(new PropertyValueFactory<>("departement"));
        
        TableColumn<Poste, Double> salaireMinCol = new TableColumn<>("Salaire Min");
        salaireMinCol.setCellValueFactory(new PropertyValueFactory<>("salaireMin"));
        
        TableColumn<Poste, Double> salaireMaxCol = new TableColumn<>("Salaire Max");
        salaireMaxCol.setCellValueFactory(new PropertyValueFactory<>("salaireMax"));
        
        table.getColumns().addAll(idCol, titreCol, typeCol, deptCol, salaireMinCol, salaireMaxCol);
        table.setItems(postesList);
        
        VBox.setVgrow(table, Priority.ALWAYS);
        view.getChildren().addAll(title, toolbar, table);
    }
    
    private void loadData() {
        postesList.clear();
        postesList.addAll(posteDAO.findAll());
    }
    
    private void showAddDialog() {
        Dialog<Poste> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un Poste");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField titreField = new TextField();
        TextField typeField = new TextField();
        TextArea descField = new TextArea();
        descField.setPrefRowCount(3);
        TextField deptField = new TextField();
        TextField niveauField = new TextField();
        TextField salaireMinField = new TextField();
        TextField salaireMaxField = new TextField();
        
        grid.add(new Label("Titre:"), 0, 0);
        grid.add(titreField, 1, 0);
        grid.add(new Label("Type:"), 0, 1);
        grid.add(typeField, 1, 1);
        grid.add(new Label("Description:"), 0, 2);
        grid.add(descField, 1, 2);
        grid.add(new Label("Département:"), 0, 3);
        grid.add(deptField, 1, 3);
        grid.add(new Label("Niveau:"), 0, 4);
        grid.add(niveauField, 1, 4);
        grid.add(new Label("Salaire Min:"), 0, 5);
        grid.add(salaireMinField, 1, 5);
        grid.add(new Label("Salaire Max:"), 0, 6);
        grid.add(salaireMaxField, 1, 6);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE), ButtonType.CANCEL);
        
        dialog.setResultConverter(btn -> {
            if (btn.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                try {
                    Poste poste = new Poste(
                        titreField.getText(), typeField.getText(), descField.getText(),
                        deptField.getText(), niveauField.getText(),
                        Double.parseDouble(salaireMinField.getText()),
                        Double.parseDouble(salaireMaxField.getText())
                    );
                    posteDAO.insert(poste);
                    loadData();
                    return poste;
                } catch (NumberFormatException e) {
                    showAlert("Erreur", "Veuillez entrer des valeurs numériques valides pour les salaires.");
                }
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private void showEditDialog() {
        Poste selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner un poste à modifier.");
            return;
        }
        
        Dialog<Poste> dialog = new Dialog<>();
        dialog.setTitle("Modifier un Poste");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField titreField = new TextField(selected.getTitre());
        TextField salaireMinField = new TextField(String.valueOf(selected.getSalaireMin()));
        TextField salaireMaxField = new TextField(String.valueOf(selected.getSalaireMax()));
        
        grid.add(new Label("Titre:"), 0, 0);
        grid.add(titreField, 1, 0);
        grid.add(new Label("Salaire Min:"), 0, 1);
        grid.add(salaireMinField, 1, 1);
        grid.add(new Label("Salaire Max:"), 0, 2);
        grid.add(salaireMaxField, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE), ButtonType.CANCEL);
        
        dialog.setResultConverter(btn -> {
            if (btn.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                try {
                    selected.setTitre(titreField.getText());
                    selected.setSalaireMin(Double.parseDouble(salaireMinField.getText()));
                    selected.setSalaireMax(Double.parseDouble(salaireMaxField.getText()));
                    posteDAO.update(selected);
                    loadData();
                    return selected;
                } catch (NumberFormatException e) {
                    showAlert("Erreur", "Veuillez entrer des valeurs numériques valides pour les salaires.");
                }
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private void deleteSelected() {
        Poste selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner un poste à supprimer.");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer le poste");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce poste ?");
        
        if (alert.showAndWait().get() == ButtonType.OK) {
            posteDAO.delete(selected.getId());
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


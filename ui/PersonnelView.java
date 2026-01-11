package com.municipal.dashboard.ui;

import com.municipal.dashboard.Personnel;
import com.municipal.dashboard.dao.PersonnelDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import java.time.LocalDateTime;
import java.util.List;

public class PersonnelView {
    private VBox view;
    private TableView<Personnel> table;
    private ObservableList<Personnel> personnelList;
    private PersonnelDAO personnelDAO;
    
    public PersonnelView() {
        this.personnelDAO = new PersonnelDAO();
        this.personnelList = FXCollections.observableArrayList();
        createView();
        loadData();
    }
    
    private void createView() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        
        Label title = new Label("Gestion du Personnel");
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
        
        TableColumn<Personnel, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<Personnel, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        
        TableColumn<Personnel, String> prenomCol = new TableColumn<>("Prénom");
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        
        TableColumn<Personnel, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        TableColumn<Personnel, String> posteCol = new TableColumn<>("Poste");
        posteCol.setCellValueFactory(new PropertyValueFactory<>("poste"));
        
        TableColumn<Personnel, String> deptCol = new TableColumn<>("Département");
        deptCol.setCellValueFactory(new PropertyValueFactory<>("departement"));
        
        TableColumn<Personnel, String> statutCol = new TableColumn<>("Statut");
        statutCol.setCellValueFactory(new PropertyValueFactory<>("statut"));
        
        table.getColumns().addAll(idCol, nomCol, prenomCol, emailCol, posteCol, deptCol, statutCol);
        table.setItems(personnelList);
        
        VBox.setVgrow(table, Priority.ALWAYS);
        view.getChildren().addAll(title, toolbar, table);
    }
    
    private void loadData() {
        personnelList.clear();
        personnelList.addAll(personnelDAO.findAll());
    }
    
    private void showAddDialog() {
        Dialog<Personnel> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un Membre du Personnel");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nomField = new TextField();
        TextField prenomField = new TextField();
        TextField emailField = new TextField();
        TextField telField = new TextField();
        TextField posteField = new TextField();
        TextField deptField = new TextField();
        TextField matriculeField = new TextField();
        ComboBox<String> statutCombo = new ComboBox<>();
        statutCombo.getItems().addAll("ACTIF", "INACTIF");
        statutCombo.setValue("ACTIF");
        
        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Prénom:"), 0, 1);
        grid.add(prenomField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Téléphone:"), 0, 3);
        grid.add(telField, 1, 3);
        grid.add(new Label("Poste:"), 0, 4);
        grid.add(posteField, 1, 4);
        grid.add(new Label("Département:"), 0, 5);
        grid.add(deptField, 1, 5);
        grid.add(new Label("Matricule:"), 0, 6);
        grid.add(matriculeField, 1, 6);
        grid.add(new Label("Statut:"), 0, 7);
        grid.add(statutCombo, 1, 7);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE), ButtonType.CANCEL);
        
        dialog.setResultConverter(btn -> {
            if (btn.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                Personnel personnel = new Personnel(
                    nomField.getText(), prenomField.getText(), emailField.getText(),
                    telField.getText(), posteField.getText(), deptField.getText(),
                    LocalDateTime.now(), statutCombo.getValue()
                );
                personnel.setMatricule(matriculeField.getText());
                personnelDAO.insert(personnel);
                loadData();
                return personnel;
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private void showEditDialog() {
        Personnel selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner un membre à modifier.");
            return;
        }
        
        Dialog<Personnel> dialog = new Dialog<>();
        dialog.setTitle("Modifier un Membre du Personnel");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField posteField = new TextField(selected.getPoste());
        TextField deptField = new TextField(selected.getDepartement());
        ComboBox<String> statutCombo = new ComboBox<>();
        statutCombo.getItems().addAll("ACTIF", "INACTIF");
        statutCombo.setValue(selected.getStatut());
        
        grid.add(new Label("Poste:"), 0, 0);
        grid.add(posteField, 1, 0);
        grid.add(new Label("Département:"), 0, 1);
        grid.add(deptField, 1, 1);
        grid.add(new Label("Statut:"), 0, 2);
        grid.add(statutCombo, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE), ButtonType.CANCEL);
        
        dialog.setResultConverter(btn -> {
            if (btn.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                selected.setPoste(posteField.getText());
                selected.setDepartement(deptField.getText());
                selected.setStatut(statutCombo.getValue());
                personnelDAO.update(selected);
                loadData();
                return selected;
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private void deleteSelected() {
        Personnel selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner un membre à supprimer.");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer le membre");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce membre du personnel ?");
        
        if (alert.showAndWait().get() == ButtonType.OK) {
            personnelDAO.delete(selected.getId());
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


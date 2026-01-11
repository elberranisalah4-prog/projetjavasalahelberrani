package com.municipal.dashboard.ui;

import com.municipal.dashboard.Note;
import com.municipal.dashboard.dao.NoteDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import java.time.LocalDateTime;
import java.util.List;

public class NotesView {
    private VBox view;
    private TableView<Note> table;
    private ObservableList<Note> notesList;
    private NoteDAO noteDAO;
    
    public NotesView() {
        this.noteDAO = new NoteDAO();
        this.notesList = FXCollections.observableArrayList();
        createView();
        loadData();
    }
    
    private void createView() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        
        Label title = new Label("Gestion des Notes");
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
        
        TableColumn<Note, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<Note, String> auteurCol = new TableColumn<>("Auteur");
        auteurCol.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        
        TableColumn<Note, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        
        TableColumn<Note, String> contenuCol = new TableColumn<>("Contenu");
        contenuCol.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        contenuCol.setPrefWidth(300);
        
        TableColumn<Note, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));
        
        table.getColumns().addAll(idCol, auteurCol, typeCol, contenuCol, dateCol);
        table.setItems(notesList);
        
        VBox.setVgrow(table, Priority.ALWAYS);
        view.getChildren().addAll(title, toolbar, table);
    }
    
    private void loadData() {
        notesList.clear();
        notesList.addAll(noteDAO.findAll());
    }
    
    private void showAddDialog() {
        Dialog<Note> dialog = new Dialog<>();
        dialog.setTitle("Ajouter une Note");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextArea contenuField = new TextArea();
        contenuField.setPrefRowCount(5);
        TextField auteurField = new TextField();
        TextField typeField = new TextField();
        
        grid.add(new Label("Contenu:"), 0, 0);
        grid.add(contenuField, 1, 0);
        grid.add(new Label("Auteur:"), 0, 1);
        grid.add(auteurField, 1, 1);
        grid.add(new Label("Type:"), 0, 2);
        grid.add(typeField, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE), ButtonType.CANCEL);
        
        dialog.setResultConverter(btn -> {
            if (btn.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                Note note = new Note(
                    contenuField.getText(),
                    LocalDateTime.now(),
                    auteurField.getText(),
                    typeField.getText()
                );
                noteDAO.insert(note);
                loadData();
                return note;
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private void showEditDialog() {
        Note selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner une note à modifier.");
            return;
        }
        
        Dialog<Note> dialog = new Dialog<>();
        dialog.setTitle("Modifier une Note");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextArea contenuField = new TextArea(selected.getContenu());
        contenuField.setPrefRowCount(5);
        
        grid.add(new Label("Contenu:"), 0, 0);
        grid.add(contenuField, 1, 0);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE), ButtonType.CANCEL);
        
        dialog.setResultConverter(btn -> {
            if (btn.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                selected.setContenu(contenuField.getText());
                noteDAO.update(selected);
                loadData();
                return selected;
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private void deleteSelected() {
        Note selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner une note à supprimer.");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer la note");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette note ?");
        
        if (alert.showAndWait().get() == ButtonType.OK) {
            noteDAO.delete(selected.getId());
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


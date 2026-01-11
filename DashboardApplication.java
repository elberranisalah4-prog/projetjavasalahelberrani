package com.municipal.dashboard;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.municipal.dashboard.ui.*;

public class DashboardApplication extends Application {
    
    private Stage primaryStage;
    private BorderPane rootLayout;
    private VBox menuBar;
    private StackPane contentArea;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Dashboard Services Publics - Système de Gestion Municipal");
        
        initRootLayout();
        showMenu();
    }
    
    private void initRootLayout() {
        rootLayout = new BorderPane();
        
        // Créer la barre de menu
        createMenuBar();
        
        // Zone de contenu
        contentArea = new StackPane();
        contentArea.setPadding(new Insets(20));
        
        rootLayout.setTop(createHeader());
        rootLayout.setLeft(menuBar);
        rootLayout.setCenter(contentArea);
        
        Scene scene = new Scene(rootLayout, 1400, 900);
        
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(700);
        primaryStage.show();
        
        // Afficher le dashboard par défaut
        showDashboard();
    }
    
    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setStyle("-fx-background-color: #2c3e50;");
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label title = new Label("🏛️ Dashboard Services Publics");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label version = new Label("Version Desktop 1.0");
        version.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 12px;");
        
        header.getChildren().addAll(title, spacer, version);
        return header;
    }
    
    private void createMenuBar() {
        menuBar = new VBox(10);
        menuBar.setPadding(new Insets(20, 10, 20, 10));
        menuBar.setStyle("-fx-background-color: #34495e; -fx-min-width: 250px;");
        
        // Boutons de menu
        Button btnDashboard = createMenuButton("📊 Dashboard", true);
        Button btnServices = createMenuButton("🔧 Services", false);
        Button btnZones = createMenuButton("📍 Zones", false);
        Button btnPersonnel = createMenuButton("👥 Personnel", false);
        Button btnPostes = createMenuButton("💼 Postes", false);
        Button btnConsommations = createMenuButton("⚡ Consommations", false);
        Button btnProductions = createMenuButton("🏭 Productions", false);
        Button btnIndicateurs = createMenuButton("📈 Indicateurs", false);
        Button btnPredictions = createMenuButton("🔮 Prédictions", false);
        Button btnDecisions = createMenuButton("📋 Décisions", false);
        Button btnNotes = createMenuButton("📝 Notes", false);
        Button btnAdmins = createMenuButton("👤 Administrateurs", false);
        
        // Actions
        btnDashboard.setOnAction(e -> showDashboard());
        btnServices.setOnAction(e -> showModule("Services"));
        btnZones.setOnAction(e -> showModule("Zones"));
        btnPersonnel.setOnAction(e -> showModule("Personnel"));
        btnPostes.setOnAction(e -> showModule("Postes"));
        btnConsommations.setOnAction(e -> showModule("Consommations"));
        btnProductions.setOnAction(e -> showModule("Productions"));
        btnIndicateurs.setOnAction(e -> showModule("Indicateurs"));
        btnPredictions.setOnAction(e -> showModule("Predictions"));
        btnDecisions.setOnAction(e -> showModule("Decisions"));
        btnNotes.setOnAction(e -> showModule("Notes"));
        btnAdmins.setOnAction(e -> showModule("Admins"));
        
        menuBar.getChildren().addAll(
            btnDashboard, btnServices, btnZones, btnPersonnel, btnPostes,
            btnConsommations, btnProductions, btnIndicateurs, btnPredictions,
            btnDecisions, btnNotes, btnAdmins
        );
    }
    
    private Button createMenuButton(String text, boolean active) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(12, 15, 12, 15));
        btn.setStyle(active 
            ? "-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-cursor: hand;"
            : "-fx-background-color: #2c3e50; -fx-text-fill: #ecf0f1; -fx-font-size: 14px; -fx-cursor: hand;"
        );
        
        btn.setOnMouseEntered(e -> {
            if (!active) {
                btn.setStyle("-fx-background-color: #3d566e; -fx-text-fill: white; -fx-font-size: 14px; -fx-cursor: hand;");
            }
        });
        
        btn.setOnMouseExited(e -> {
            if (!active) {
                btn.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: #ecf0f1; -fx-font-size: 14px; -fx-cursor: hand;");
            }
        });
        
        return btn;
    }
    
    private void showMenu() {
        // Menu déjà créé dans createMenuBar
    }
    
    private void showDashboard() {
        DashboardView dashboardView = new DashboardView();
        contentArea.getChildren().clear();
        contentArea.getChildren().add(dashboardView.getView());
    }
    
    private void showModule(String moduleName) {
        contentArea.getChildren().clear();
        
        switch (moduleName) {
            case "Services":
                ServicesView servicesView = new ServicesView();
                contentArea.getChildren().add(servicesView.getView());
                break;
            case "Zones":
                ZonesView zonesView = new ZonesView();
                contentArea.getChildren().add(zonesView.getView());
                break;
            case "Personnel":
                PersonnelView personnelView = new PersonnelView();
                contentArea.getChildren().add(personnelView.getView());
                break;
            case "Postes":
                PostesView postesView = new PostesView();
                contentArea.getChildren().add(postesView.getView());
                break;
            case "Consommations":
                ConsommationsView consommationsView = new ConsommationsView();
                contentArea.getChildren().add(consommationsView.getView());
                break;
            case "Productions":
                ProductionsView productionsView = new ProductionsView();
                contentArea.getChildren().add(productionsView.getView());
                break;
            case "Indicateurs":
                IndicateursView indicateursView = new IndicateursView();
                contentArea.getChildren().add(indicateursView.getView());
                break;
            case "Predictions":
                PredictionsView predictionsView = new PredictionsView();
                contentArea.getChildren().add(predictionsView.getView());
                break;
            case "Decisions":
                DecisionsView decisionsView = new DecisionsView();
                contentArea.getChildren().add(decisionsView.getView());
                break;
            case "Notes":
                NotesView notesView = new NotesView();
                contentArea.getChildren().add(notesView.getView());
                break;
            case "Admins":
                AdminsView adminsView = new AdminsView();
                contentArea.getChildren().add(adminsView.getView());
                break;
            default:
                Label label = new Label("Module " + moduleName + " en cours de développement");
                label.setStyle("-fx-font-size: 18px;");
                contentArea.getChildren().add(label);
        }
    }
}


package com.municipal.dashboard;

import javafx.application.Application;

public class MainDesktop {
    
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("    DASHBOARD SERVICES PUBLICS - APPLICATION DESKTOP");
        System.out.println("    Système de Gestion Municipal");
        System.out.println("=".repeat(60));
        
        // Initialisation de la base de données
        DatabaseManager dbManager = DatabaseManager.getInstance();
        
        try {
            // Lancement de l'application JavaFX
            Application.launch(DashboardApplication.class, args);
        } catch (Exception e) {
            System.err.println("\n✗ ERREUR lors de l'exécution: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Fermeture de la connexion à la base de données
            if (DatabaseManager.getInstance() != null) {
                DatabaseManager.getInstance().close();
            }
        }
    }
}


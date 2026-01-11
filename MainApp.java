package com.municipal.dashboard;

import com.municipal.dashboard.service.GestionnaireStock;
import com.municipal.dashboard.exception.StockException;

/**
 * Point d'entrée principal de l'application
 * Respecte la structure Maven standard
 */
public class MainApp {
    
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("    DASHBOARD SERVICES PUBLICS");
        System.out.println("    Système de Gestion Municipal");
        System.out.println("=".repeat(70));
        System.out.println();
        
        try {
            // Initialisation de la base de données
            DatabaseManager dbManager = DatabaseManager.getInstance();
            System.out.println("✓ Base de données initialisée");
            
            // Création du gestionnaire de stock
            GestionnaireStock gestionnaire = new GestionnaireStock();
            
            // Démonstration des fonctionnalités
            System.out.println("\n" + "-".repeat(70));
            System.out.println("DÉMONSTRATION DES FONCTIONNALITÉS");
            System.out.println("-".repeat(70));
            
            // Exemples d'utilisation
            gestionnaire.afficherTousLesProduits();
            gestionnaire.calculerStatistiques();
            
            System.out.println("\n✓ Application démarrée avec succès!");
            
        } catch (Exception e) {
            System.err.println("\n✗ ERREUR: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Fermeture de la connexion
            if (DatabaseManager.getInstance() != null) {
                DatabaseManager.getInstance().close();
            }
        }
    }
}


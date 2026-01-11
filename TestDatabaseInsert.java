package com.municipal.dashboard;

import com.municipal.dashboard.dao.ConsommationDAO;
import java.time.LocalDateTime;
import java.sql.Connection;

/**
 * Script de test pour vérifier que les insertions dans la base de données fonctionnent
 */
public class TestDatabaseInsert {
    public static void main(String[] args) {
        System.out.println("=== TEST D'INSERTION DANS LA BASE DE DONNÉES ===");
        
        // Initialiser la base de données
        DatabaseManager dbManager = DatabaseManager.getInstance();
        Connection conn = dbManager.getConnection();
        
        if (conn == null) {
            System.err.println("✗ ERREUR: Impossible d'obtenir une connexion à la base de données!");
            System.err.println("   Vérifiez que MySQL est démarré et que les paramètres de connexion sont corrects.");
            return;
        }
        
        System.out.println("✓ Connexion à la base de données établie");
        
        // Vérifier que la table existe
        try {
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery("SHOW TABLES LIKE 'consommation'");
            if (rs.next()) {
                System.out.println("✓ Table 'consommation' existe");
            } else {
                System.err.println("✗ ERREUR: La table 'consommation' n'existe pas!");
                System.err.println("   La table devrait être créée automatiquement au démarrage.");
                return;
            }
        } catch (java.sql.SQLException e) {
            System.err.println("✗ ERREUR lors de la vérification de la table: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        
        // Tester une insertion
        System.out.println("\n--- Test d'insertion ---");
        Consommation testConsommation = new Consommation(
            100.5,
            LocalDateTime.now(),
            "Test Service",
            "Test Zone"
        );
        
        System.out.println("Données à insérer:");
        System.out.println("  valeur: " + testConsommation.getValeur());
        System.out.println("  serviceType: " + testConsommation.getServiceType());
        System.out.println("  zone: " + testConsommation.getZone());
        System.out.println("  date: " + testConsommation.getDate());
        
        ConsommationDAO dao = new ConsommationDAO();
        dao.insert(testConsommation);
        
        if (testConsommation.getId() != null) {
            System.out.println("\n✓ INSERTION RÉUSSIE!");
            System.out.println("  ID généré: " + testConsommation.getId());
            
            // Vérifier que la donnée est bien dans la base
            System.out.println("\n--- Vérification de la récupération ---");
            Consommation retrieved = dao.findById(testConsommation.getId());
            if (retrieved != null) {
                System.out.println("✓ Donnée récupérée avec succès:");
                System.out.println("  ID: " + retrieved.getId());
                System.out.println("  valeur: " + retrieved.getValeur());
                System.out.println("  serviceType: " + retrieved.getServiceType());
                System.out.println("  zone: " + retrieved.getZone());
                System.out.println("  date: " + retrieved.getDate());
            } else {
                System.err.println("✗ ERREUR: La donnée insérée n'a pas pu être récupérée!");
            }
        } else {
            System.err.println("\n✗ ÉCHEC DE L'INSERTION!");
            System.err.println("   Aucun ID n'a été généré, ce qui indique que l'insertion a échoué.");
            System.err.println("   Vérifiez les logs ci-dessus pour plus de détails.");
        }
        
        // Afficher toutes les consommations
        System.out.println("\n--- Liste de toutes les consommations ---");
        java.util.List<Consommation> all = dao.findAll();
        System.out.println("Nombre total de consommations: " + all.size());
        for (Consommation c : all) {
            System.out.println("  ID=" + c.getId() + 
                             ", valeur=" + c.getValeur() + 
                             ", serviceType=" + c.getServiceType() + 
                             ", zone=" + c.getZone());
        }
    }
}


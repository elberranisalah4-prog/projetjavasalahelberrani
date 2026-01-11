package com.municipal.dashboard;

import com.municipal.dashboard.dao.*;
import java.util.List;

public class TestVerification {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("   VERIFICATION DU PROJET DASHBOARD SERVICES PUBLICS");
        System.out.println("=".repeat(60));
        System.out.println();
        
        int testsReussis = 0;
        int testsTotal = 0;
        
        // Test 1: Vérification de DatabaseManager
        System.out.println("Test 1: Initialisation de DatabaseManager...");
        testsTotal++;
        try {
            DatabaseManager dbManager = DatabaseManager.getInstance();
            if (dbManager != null) {
                System.out.println("   ✓ DatabaseManager initialisé avec succès");
                testsReussis++;
            } else {
                System.out.println("   ✗ DatabaseManager n'a pas pu être initialisé");
            }
        } catch (Exception e) {
            System.out.println("   ✗ Erreur: " + e.getMessage());
        }
        System.out.println();
        
        // Test 2: Vérification des DAOs
        System.out.println("Test 2: Vérification des DAOs...");
        testsTotal++;
        try {
            ServiceDAO serviceDAO = new ServiceDAO();
            ZoneDAO zoneDAO = new ZoneDAO();
            AdminDAO adminDAO = new AdminDAO();
            PersonnelDAO personnelDAO = new PersonnelDAO();
            PosteDAO posteDAO = new PosteDAO();
            ConsommationDAO consommationDAO = new ConsommationDAO();
            ProductionDAO productionDAO = new ProductionDAO();
            IndicatorDAO indicatorDAO = new IndicatorDAO();
            PredictionDAO predictionDAO = new PredictionDAO();
            DecisionDAO decisionDAO = new DecisionDAO();
            NoteDAO noteDAO = new NoteDAO();
            
            System.out.println("   ✓ Tous les DAOs créés avec succès (11 DAOs)");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("   ✗ Erreur lors de la création des DAOs: " + e.getMessage());
        }
        System.out.println();
        
        // Test 3: Test de connexion à la base de données
        System.out.println("Test 3: Test de connexion à la base de données...");
        testsTotal++;
        try {
            DatabaseManager dbManager = DatabaseManager.getInstance();
            var connection = dbManager.getConnection();
            if (connection != null && !connection.isClosed()) {
                System.out.println("   ✓ Connexion à MySQL établie avec succès");
                testsReussis++;
            } else {
                System.out.println("   ✗ Connexion échouée");
            }
        } catch (Exception e) {
            System.out.println("   ✗ Erreur de connexion: " + e.getMessage());
            System.out.println("   ⚠ Assurez-vous que MySQL est démarré et que la base 'services_publics' existe");
        }
        System.out.println();
        
        // Test 4: Test des opérations de base (findAll)
        System.out.println("Test 4: Test des opérations de lecture...");
        testsTotal++;
        try {
            ServiceDAO serviceDAO = new ServiceDAO();
            List<Service> services = serviceDAO.findAll();
            System.out.println("   ✓ Lecture des services réussie (" + services.size() + " enregistrements)");
            
            ZoneDAO zoneDAO = new ZoneDAO();
            List<Zone> zones = zoneDAO.findAll();
            System.out.println("   ✓ Lecture des zones réussie (" + zones.size() + " enregistrements)");
            
            testsReussis++;
        } catch (Exception e) {
            System.out.println("   ✗ Erreur lors de la lecture: " + e.getMessage());
        }
        System.out.println();
        
        // Test 5: Vérification des modèles
        System.out.println("Test 5: Vérification des modèles de données...");
        testsTotal++;
        try {
            Service service = new Service("TEST", "Service Test", "Description test");
            Zone zone = new Zone("Zone Test", "ZT001", "Description zone");
            Admin admin = new Admin("Doe", "John", "john@test.com", "pass", "ADMIN", "123456");
            
            System.out.println("   ✓ Modèles Service, Zone, Admin créés avec succès");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("   ✗ Erreur lors de la création des modèles: " + e.getMessage());
        }
        System.out.println();
        
        // Résumé
        System.out.println("=".repeat(60));
        System.out.println("   RESUME DES TESTS");
        System.out.println("=".repeat(60));
        System.out.println("Tests réussis: " + testsReussis + " / " + testsTotal);
        System.out.println("Taux de réussite: " + (testsReussis * 100 / testsTotal) + "%");
        System.out.println();
        
        if (testsReussis == testsTotal) {
            System.out.println("✓ TOUS LES TESTS SONT PASSES AVEC SUCCES!");
            System.out.println("Le projet est prêt à être utilisé.");
        } else {
            System.out.println("⚠ Certains tests ont échoué. Vérifiez les erreurs ci-dessus.");
        }
        System.out.println("=".repeat(60));
        
        // Fermeture
        try {
            DatabaseManager.getInstance().close();
        } catch (Exception e) {
            // Ignorer les erreurs de fermeture
        }
    }
}


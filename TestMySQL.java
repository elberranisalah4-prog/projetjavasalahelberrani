package com.municipal.dashboard;

import java.sql.*;

public class TestMySQL {
    
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("    TEST DE CONNEXION MYSQL");
        System.out.println("    Vérification phpMyAdmin");
        System.out.println("=".repeat(60));
        System.out.println();
        
        String host = "localhost";
        String port = "3306";
        String dbName = "services_publics";
        String user = "root";
        String password = "";
        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false&serverTimezone=UTC";
        
        System.out.println("Configuration de connexion:");
        System.out.println("  Host: " + host);
        System.out.println("  Port: " + port);
        System.out.println("  Database: " + dbName);
        System.out.println("  User: " + user);
        System.out.println("  Password: " + (password.isEmpty() ? "(vide)" : "***"));
        System.out.println();
        
        try {
            // Charger le driver MySQL
            System.out.println("1. Chargement du driver MySQL...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("   ✓ Driver chargé avec succès");
            System.out.println();
            
            // Test de connexion
            System.out.println("2. Tentative de connexion à la base de données...");
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("   ✓ Connexion réussie!");
            System.out.println();
            
            // Test de la base de données
            System.out.println("3. Vérification de la base de données '" + dbName + "'...");
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet databases = metaData.getCatalogs();
            boolean dbExists = false;
            while (databases.next()) {
                String databaseName = databases.getString("TABLE_CAT");
                if (databaseName.equals(dbName)) {
                    dbExists = true;
                    break;
                }
            }
            databases.close();
            
            if (dbExists) {
                System.out.println("   ✓ La base de données '" + dbName + "' existe");
            } else {
                System.out.println("   ✗ La base de données '" + dbName + "' n'existe pas");
                System.out.println();
                System.out.println("   SOLUTION: Créez la base dans phpMyAdmin:");
                System.out.println("   1. Ouvrez http://localhost/phpmyadmin");
                System.out.println("   2. Cliquez sur 'Nouvelle base de données'");
                System.out.println("   3. Nom: " + dbName);
                System.out.println("   4. Interclassement: utf8mb4_general_ci");
                System.out.println("   5. Cliquez sur 'Créer'");
                conn.close();
                return;
            }
            System.out.println();
            
            // Test des privilèges
            System.out.println("4. Vérification des privilèges de l'utilisateur...");
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("SELECT 1");
                System.out.println("   ✓ L'utilisateur a les droits de lecture");
            } catch (SQLException e) {
                System.out.println("   ✗ Erreur de lecture: " + e.getMessage());
            }
            
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS test_privileges (id INT)");
                stmt.execute("DROP TABLE IF EXISTS test_privileges");
                System.out.println("   ✓ L'utilisateur a les droits d'écriture");
            } catch (SQLException e) {
                System.out.println("   ✗ Erreur d'écriture: " + e.getMessage());
                System.out.println();
                System.out.println("   SOLUTION: Donnez tous les privilèges à 'root' sur '" + dbName + "'");
            }
            System.out.println();
            
            // Lister les tables existantes
            System.out.println("5. Tables existantes dans la base de données:");
            ResultSet tables = metaData.getTables(dbName, null, "%", new String[]{"TABLE"});
            int tableCount = 0;
            while (tables.next()) {
                tableCount++;
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("   - " + tableName);
            }
            tables.close();
            
            if (tableCount == 0) {
                System.out.println("   (Aucune table - le projet les créera au démarrage)");
            } else {
                System.out.println("   Total: " + tableCount + " table(s)");
            }
            System.out.println();
            
            // Résumé
            System.out.println("=".repeat(60));
            System.out.println("✓ TOUS LES TESTS SONT RÉUSSIS!");
            System.out.println("=".repeat(60));
            System.out.println();
            System.out.println("Votre configuration MySQL/phpMyAdmin est correcte.");
            System.out.println("Le projet peut maintenant démarrer normalement.");
            System.out.println();
            
            conn.close();
            
        } catch (ClassNotFoundException e) {
            System.err.println();
            System.err.println("✗ ERREUR: Driver MySQL non trouvé!");
            System.err.println("   Téléchargez mysql-connector-java.jar et placez-le dans:");
            System.err.println("   lib/mysql-connector-java.jar");
            System.err.println();
        } catch (SQLException e) {
            System.err.println();
            System.err.println("✗ ERREUR de connexion: " + e.getMessage());
            System.err.println();
            
            if (e.getMessage().contains("Access denied")) {
                System.err.println("SOLUTION:");
                System.err.println("  1. Vérifiez le mot de passe de l'utilisateur 'root'");
                System.err.println("  2. Si MySQL a un mot de passe, modifiez DB_PASSWORD dans:");
                System.err.println("     - DatabaseManager.java");
                System.err.println("     - Database.java");
            } else if (e.getMessage().contains("Unknown database")) {
                System.err.println("SOLUTION:");
                System.err.println("  1. Créez la base de données dans phpMyAdmin:");
                System.err.println("     - Ouvrez http://localhost/phpmyadmin");
                System.err.println("     - Cliquez sur 'Nouvelle base de données'");
                System.err.println("     - Nom: " + dbName);
                System.err.println("     - Interclassement: utf8mb4_general_ci");
                System.err.println("     - Cliquez sur 'Créer'");
            } else if (e.getMessage().contains("Communications link failure")) {
                System.err.println("SOLUTION:");
                System.err.println("  1. Vérifiez que MySQL est démarré");
                System.err.println("  2. Vérifiez que MySQL écoute sur le port 3306");
                System.err.println("  3. Vérifiez dans les services Windows (Services → MySQL)");
            }
            System.err.println();
        }
    }
}


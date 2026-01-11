package com.municipal.dashboard;

import java.sql.*;

public class DatabaseManager {
    // Configuration MySQL pour XAMPP - Utilise DatabaseConfig pour une configuration sécurisée
    // Les valeurs sont chargées depuis db_config.properties ou utilisent les valeurs par défaut
    
    private Connection connection;
    private static DatabaseManager instance;

    private DatabaseManager() {
        initializeDatabase();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void initializeDatabase() {
        try {
            // Essayer de charger le driver MySQL (nouvelle version 8.x)
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e1) {
                // Essayer l'ancienne version si la nouvelle n'est pas disponible
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (ClassNotFoundException e2) {
                    System.err.println("✗ ERREUR: Driver MySQL non trouvé!");
                    System.err.println("  Tentatives:");
                    System.err.println("    - com.mysql.cj.jdbc.Driver: ÉCHEC");
                    System.err.println("    - com.mysql.jdbc.Driver: ÉCHEC");
                    System.err.println("\n💡 Solutions:");
                    System.err.println("  1. Vérifiez que mysql-connector-java.jar est dans com/municipal/dashboard/lib/");
                    System.err.println("  2. Vérifiez que le JAR est inclus dans le classpath lors de l'exécution:");
                    System.err.println("     java -cp \".;com\\municipal\\dashboard\\lib\\mysql-connector-java.jar\" ...");
                    throw e2;
                }
            }
            
            // Charger la configuration depuis DatabaseConfig
            String dbHost = DatabaseConfig.getHost();
            String dbPort = DatabaseConfig.getPort();
            String dbName = DatabaseConfig.getDatabaseName();
            String dbUser = DatabaseConfig.getUser();
            String dbPassword = DatabaseConfig.getPassword();
            
            // Créer la base de données si elle n'existe pas (compatible phpMyAdmin)
            String baseUrl = "jdbc:mysql://" + dbHost + ":" + dbPort 
                    + "?useSSL=" + DatabaseConfig.useSSL()
                    + "&serverTimezone=" + DatabaseConfig.getTimezone()
                    + "&useUnicode=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true";
            try (Connection tempConn = DriverManager.getConnection(baseUrl, dbUser, dbPassword);
                 Statement stmt = tempConn.createStatement()) {
                // Créer la base de données avec charset UTF-8 pour compatibilité phpMyAdmin
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName 
                        + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
                System.out.println("✓ Base de données '" + dbName + "' vérifiée/créée avec succès");
            } catch (SQLException e) {
                System.err.println("Erreur lors de la création de la base de données: " + e.getMessage());
                // Ne pas bloquer si la base existe déjà
                if (!e.getMessage().contains("already exists")) {
                    throw e;
                }
            }
            
            connection = DriverManager.getConnection(DatabaseConfig.getConnectionURL(), dbUser, dbPassword);
            // S'assurer que auto-commit est activé pour que les insertions soient immédiatement validées
            connection.setAutoCommit(true);
            System.out.println("✓ Connexion établie avec auto-commit activé");
            createAllTables();
            System.out.println("✓ Base de données MySQL initialisée avec succès!");
        } catch (ClassNotFoundException e) {
            System.err.println("✗ ERREUR: Driver MySQL non trouvé!");
            System.err.println("Téléchargez mysql-connector-java.jar et ajoutez-le au classpath");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la connexion à MySQL: " + e.getMessage());
            if (e.getMessage().contains("No suitable driver")) {
                System.err.println("\n💡 Le driver MySQL n'est pas dans le classpath!");
                System.err.println("  Vérifiez que mysql-connector-java.jar est inclus dans le classpath.");
            }
            e.printStackTrace();
        }
    }

    private void createAllTables() {
        createServiceDataTable();
        createServiceTable();
        createZoneTable();
        createAdminTable();
        createPersonnelTable();
        createPosteTable();
        createConsommationTable();
        createProductionTable();
        createIndicatorTable();
        createPredictionTable();
        createDecisionTable();
        createNoteTable();
        createProduitTable();
        // Note: Les tables Sports (teams, stadiums, matches, transports, accommodations) 
        // ne font pas partie de ce projet et ont été retirées
    }

    private void createServiceDataTable() {
        String sql = "CREATE TABLE IF NOT EXISTS service_data (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "service_type VARCHAR(50) NOT NULL, " +
                "date DATETIME NOT NULL, " +
                "consommation DOUBLE, " +
                "production DOUBLE, " +
                "cout DOUBLE, " +
                "zone VARCHAR(100), " +
                "notes TEXT" +
                ")";
        executeCreateTable(sql, "service_data");
    }

    private void createServiceTable() {
        String sql = "CREATE TABLE IF NOT EXISTS service (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "type VARCHAR(50) NOT NULL, " +
                "nom VARCHAR(100) NOT NULL, " +
                "description TEXT" +
                ")";
        executeCreateTable(sql, "service");
    }

    private void createZoneTable() {
        String sql = "CREATE TABLE IF NOT EXISTS zone (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "nom VARCHAR(100) NOT NULL, " +
                "code VARCHAR(20) UNIQUE, " +
                "description TEXT" +
                ")";
        executeCreateTable(sql, "zone");
    }

    private void createAdminTable() {
        String sql = "CREATE TABLE IF NOT EXISTS admin (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "nom VARCHAR(100) NOT NULL, " +
                "prenom VARCHAR(100) NOT NULL, " +
                "email VARCHAR(150) UNIQUE NOT NULL, " +
                "mot_de_passe VARCHAR(255) NOT NULL, " +
                "role VARCHAR(50), " +
                "telephone VARCHAR(20), " +
                "date_creation DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "statut VARCHAR(20) DEFAULT 'ACTIF', " +
                "derniere_connexion DATETIME" +
                ")";
        executeCreateTable(sql, "admin");
    }

    private void createPersonnelTable() {
        String sql = "CREATE TABLE IF NOT EXISTS personnel (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "nom VARCHAR(100) NOT NULL, " +
                "prenom VARCHAR(100) NOT NULL, " +
                "email VARCHAR(150) UNIQUE, " +
                "telephone VARCHAR(20), " +
                "poste VARCHAR(100), " +
                "departement VARCHAR(100), " +
                "date_embauche DATETIME, " +
                "statut VARCHAR(20) DEFAULT 'ACTIF', " +
                "matricule VARCHAR(50) UNIQUE" +
                ")";
        executeCreateTable(sql, "personnel");
    }

    private void createPosteTable() {
        String sql = "CREATE TABLE IF NOT EXISTS poste (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "titre VARCHAR(100) NOT NULL, " +
                "type VARCHAR(50), " +
                "description TEXT, " +
                "departement VARCHAR(100), " +
                "niveau VARCHAR(50), " +
                "salaire_min DOUBLE, " +
                "salaire_max DOUBLE" +
                ")";
        executeCreateTable(sql, "poste");
    }

    private void createConsommationTable() {
        String sql = "CREATE TABLE IF NOT EXISTS consommation (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "valeur DOUBLE NOT NULL, " +
                "date DATETIME NOT NULL, " +
                "service_type VARCHAR(50), " +
                "zone VARCHAR(100)" +
                ")";
        executeCreateTable(sql, "consommation");
    }

    private void createProductionTable() {
        String sql = "CREATE TABLE IF NOT EXISTS production (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "valeur DOUBLE NOT NULL, " +
                "date DATETIME NOT NULL, " +
                "service_type VARCHAR(50), " +
                "zone VARCHAR(100)" +
                ")";
        executeCreateTable(sql, "production");
    }

    private void createIndicatorTable() {
        String sql = "CREATE TABLE IF NOT EXISTS indicator (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "nom VARCHAR(100) NOT NULL, " +
                "type VARCHAR(50), " +
                "valeur DOUBLE, " +
                "valeur_cible DOUBLE, " +
                "date_calcul DATETIME, " +
                "unite VARCHAR(20), " +
                "description TEXT, " +
                "statut VARCHAR(20)" +
                ")";
        executeCreateTable(sql, "indicator");
    }

    private void createPredictionTable() {
        String sql = "CREATE TABLE IF NOT EXISTS prediction (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "service_type VARCHAR(50), " +
                "date_prediction DATETIME, " +
                "valeur_predite DOUBLE, " +
                "confiance DOUBLE, " +
                "zone VARCHAR(100), " +
                "type_prediction VARCHAR(50), " +
                "recommandation TEXT, " +
                "date_calcul DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ")";
        executeCreateTable(sql, "prediction");
    }

    private void createDecisionTable() {
        String sql = "CREATE TABLE IF NOT EXISTS decision (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "titre VARCHAR(200) NOT NULL, " +
                "description TEXT, " +
                "type VARCHAR(50), " +
                "statut VARCHAR(50), " +
                "date_decision DATETIME, " +
                "date_application DATETIME, " +
                "auteur VARCHAR(100), " +
                "service_concerne VARCHAR(100), " +
                "zone_concernee VARCHAR(100), " +
                "justification TEXT" +
                ")";
        executeCreateTable(sql, "decision");
    }

    private void createNoteTable() {
        String sql = "CREATE TABLE IF NOT EXISTS note (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "contenu TEXT NOT NULL, " +
                "date_creation DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "auteur VARCHAR(100), " +
                "type VARCHAR(50)" +
                ")";
        executeCreateTable(sql, "note");
    }
    
    private void createProduitTable() {
        String sql = "CREATE TABLE IF NOT EXISTS produit (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "nom VARCHAR(100) NOT NULL, " +
                "type VARCHAR(50), " +
                "valeur DOUBLE, " +
                "date DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "zone VARCHAR(100), " +
                "description TEXT" +
                ")";
        executeCreateTable(sql, "produit");
    }
    
    // Note: Les méthodes pour créer les tables Sports (teams, stadiums, matches, transports, accommodations)
    // ont été retirées car elles ne font pas partie de ce projet de gestion municipale

    private void executeCreateTable(String sql, String tableName) {
        try (Statement stmt = connection.createStatement()) {
            // Ajouter ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 pour compatibilité phpMyAdmin
            if (!sql.toUpperCase().contains("ENGINE")) {
                sql = sql.replace(")", " ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci)");
            }
            stmt.execute(sql);
            System.out.println("  ✓ Table '" + tableName + "' créée/vérifiée");
        } catch (SQLException e) {
            // Ne pas afficher d'erreur si la table existe déjà
            if (!e.getMessage().contains("already exists") && !e.getMessage().contains("Duplicate")) {
                System.err.println("⚠ Erreur lors de la création de la table " + tableName + ": " + e.getMessage());
            }
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("⚠ Reconnexion à la base de données...");
                connection = DriverManager.getConnection(
                    DatabaseConfig.getConnectionURL(), 
                    DatabaseConfig.getUser(), 
                    DatabaseConfig.getPassword()
                );
                // S'assurer que auto-commit est activé
                connection.setAutoCommit(true);
                System.out.println("✓ Reconnexion réussie");
            }
            // Vérifier que la connexion est valide
            if (connection.isValid(2)) {
                return connection;
            } else {
                System.err.println("⚠ Connexion invalide, tentative de reconnexion...");
                connection = DriverManager.getConnection(
                    DatabaseConfig.getConnectionURL(), 
                    DatabaseConfig.getUser(), 
                    DatabaseConfig.getPassword()
                );
                connection.setAutoCommit(true);
                return connection;
            }
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la reconnexion: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture: " + e.getMessage());
        }
    }
}


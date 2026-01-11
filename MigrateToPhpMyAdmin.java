package com.municipal.dashboard;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MigrateToPhpMyAdmin {
    
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("    MIGRATION VERS PHPMYADMIN");
        System.out.println("    Export complet de la base de données MySQL");
        System.out.println("=".repeat(60));
        System.out.println();
        
        DatabaseManager dbManager = DatabaseManager.getInstance();
        Connection conn = dbManager.getConnection();
        
        if (conn == null) {
            System.err.println("❌ Erreur: Impossible de se connecter à la base de données MySQL");
            System.err.println("   Vérifiez que MySQL est démarré et que la base 'services_publics' existe");
            return;
        }
        
        String fileName = "services_publics_export_" + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".sql";
        
        try (FileWriter writer = new FileWriter(fileName)) {
            System.out.println("📝 Génération du fichier SQL: " + fileName);
            System.out.println();
            
            // En-tête du fichier SQL
            writer.write("-- ============================================================\n");
            writer.write("-- Export SQL de la base de données services_publics\n");
            writer.write("-- Date: " + LocalDateTime.now() + "\n");
            writer.write("-- Compatible avec phpMyAdmin\n");
            writer.write("-- ============================================================\n\n");
            
            // Création de la base de données
            writer.write("-- Création de la base de données\n");
            writer.write("CREATE DATABASE IF NOT EXISTS `services_publics` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;\n");
            writer.write("USE `services_publics`;\n\n");
            
            // Liste des tables à exporter
            String[] tables = {
                "admin", "service", "zone", "personnel", "poste", 
                "consommation", "production", "indicator", "prediction", 
                "decision", "note", "service_data"
            };
            
            System.out.println("📊 Export des tables:");
            
            for (String tableName : tables) {
                try {
                    System.out.print("  - " + tableName + "... ");
                    
                    // Vérifier si la table existe
                    DatabaseMetaData metaData = conn.getMetaData();
                    ResultSet tablesRS = metaData.getTables(null, null, tableName, null);
                    
                    if (!tablesRS.next()) {
                        System.out.println("table n'existe pas, ignorée");
                        continue;
                    }
                    
                    // Exporter la structure de la table
                    writer.write("\n-- ============================================================\n");
                    writer.write("-- Structure de la table `" + tableName + "`\n");
                    writer.write("-- ============================================================\n\n");
                    
                    // Récupérer la structure CREATE TABLE
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE `" + tableName + "`")) {
                        if (rs.next()) {
                            String createTable = rs.getString(2);
                            writer.write("DROP TABLE IF EXISTS `" + tableName + "`;\n");
                            writer.write(createTable + ";\n\n");
                        }
                    }
                    
                    // Exporter les données
                    writer.write("-- Données de la table `" + tableName + "`\n");
                    
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery("SELECT * FROM `" + tableName + "`")) {
                        
                        ResultSetMetaData rsMetaData = rs.getMetaData();
                        int columnCount = rsMetaData.getColumnCount();
                        int rowCount = 0;
                        
                        while (rs.next()) {
                            rowCount++;
                            writer.write("INSERT INTO `" + tableName + "` (");
                            
                            // Noms des colonnes
                            for (int i = 1; i <= columnCount; i++) {
                                if (i > 1) writer.write(", ");
                                writer.write("`" + rsMetaData.getColumnName(i) + "`");
                            }
                            
                            writer.write(") VALUES (");
                            
                            // Valeurs
                            for (int i = 1; i <= columnCount; i++) {
                                if (i > 1) writer.write(", ");
                                
                                Object value = rs.getObject(i);
                                if (value == null) {
                                    writer.write("NULL");
                                } else {
                                    String strValue = value.toString();
                                    int columnType = rsMetaData.getColumnType(i);
                                    
                                    // Gérer les différents types de données
                                    if (columnType == Types.VARCHAR || columnType == Types.CHAR || 
                                        columnType == Types.LONGVARCHAR ||
                                        columnType == Types.TIMESTAMP || columnType == Types.DATE || 
                                        columnType == Types.TIME || 
                                        columnType == Types.CLOB || columnType == Types.NCLOB) {
                                        // Échapper les apostrophes et guillemets
                                        strValue = strValue.replace("\\", "\\\\")
                                                          .replace("'", "\\'")
                                                          .replace("\"", "\\\"")
                                                          .replace("\n", "\\n")
                                                          .replace("\r", "\\r");
                                        writer.write("'" + strValue + "'");
                                    } else {
                                        writer.write(strValue);
                                    }
                                }
                            }
                            
                            writer.write(");\n");
                        }
                        
                        System.out.println(rowCount + " ligne(s) exportée(s)");
                    }
                    
                    writer.write("\n");
                    
                } catch (SQLException e) {
                    System.out.println("ERREUR: " + e.getMessage());
                    writer.write("-- Erreur lors de l'export de la table " + tableName + ": " + e.getMessage() + "\n\n");
                }
            }
            
            writer.write("-- ============================================================\n");
            writer.write("-- Fin de l'export\n");
            writer.write("-- ============================================================\n");
            
            System.out.println();
            System.out.println("✅ Export terminé avec succès!");
            System.out.println();
            System.out.println("📁 Fichier généré: " + fileName);
            System.out.println();
            System.out.println("📋 Instructions pour importer dans phpMyAdmin:");
            System.out.println("   1. Ouvrez phpMyAdmin dans votre navigateur");
            System.out.println("   2. Sélectionnez la base de données 'services_publics' (ou créez-la)");
            System.out.println("   3. Cliquez sur l'onglet 'Importer'");
            System.out.println("   4. Cliquez sur 'Choisir un fichier' et sélectionnez: " + fileName);
            System.out.println("   5. Cliquez sur 'Exécuter'");
            System.out.println();
            System.out.println("💡 Alternative: Utilisez la ligne de commande MySQL:");
            System.out.println("   mysql -u root -p services_publics < " + fileName);
            
        } catch (IOException e) {
            System.err.println("❌ Erreur lors de l'écriture du fichier: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (dbManager != null) {
                dbManager.close();
            }
        }
    }
}


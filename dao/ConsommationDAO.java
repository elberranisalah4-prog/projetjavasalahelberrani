package com.municipal.dashboard.dao;

import com.municipal.dashboard.Consommation;
import com.municipal.dashboard.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsommationDAO {
    private DatabaseManager dbManager;

    public ConsommationDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public void insert(Consommation consommation) {
        System.out.println("\n=== [ConsommationDAO.insert] DÉBUT ===");
        Connection conn = dbManager.getConnection();
        if (conn == null) {
            System.err.println("✗ ERREUR CRITIQUE: Connexion à la base de données est NULL!");
            System.err.println("   Vérifiez que MySQL est démarré et que la connexion est établie.");
            return;
        }
        
        try {
            // Vérifier que la connexion est valide
            if (conn.isClosed()) {
                System.err.println("✗ ERREUR: La connexion est fermée!");
                return;
            }
            
            // Vérifier que auto-commit est activé
            boolean autoCommit = conn.getAutoCommit();
            System.out.println("  [DEBUG] Auto-commit: " + autoCommit);
            if (!autoCommit) {
                System.out.println("  [DEBUG] Activation de auto-commit...");
                conn.setAutoCommit(true);
            }
            
            // Vérifier que la table existe
            try (Statement checkStmt = conn.createStatement()) {
                ResultSet rs = checkStmt.executeQuery("SELECT COUNT(*) FROM consommation");
                rs.next();
                int count = rs.getInt(1);
                System.out.println("  [DEBUG] Nombre de consommations existantes: " + count);
            } catch (SQLException e) {
                System.err.println("✗ ERREUR: La table 'consommation' n'existe pas ou n'est pas accessible!");
                System.err.println("  Message: " + e.getMessage());
                return;
            }
            
            String sql = "INSERT INTO consommation (valeur, date, service_type, zone) VALUES (?, ?, ?, ?)";
            System.out.println("  [DEBUG] SQL: " + sql);
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                Double valeur = consommation.getValeur() != null ? consommation.getValeur() : 0.0;
                Timestamp dateTimestamp;
                if (consommation.getDate() != null) {
                    dateTimestamp = Timestamp.valueOf(consommation.getDate());
                } else {
                    dateTimestamp = Timestamp.valueOf(java.time.LocalDateTime.now());
                }
                String serviceType = consommation.getServiceType() != null ? consommation.getServiceType() : "";
                String zone = consommation.getZone() != null ? consommation.getZone() : "";
                
                pstmt.setDouble(1, valeur);
                pstmt.setTimestamp(2, dateTimestamp);
                pstmt.setString(3, serviceType);
                pstmt.setString(4, zone);
                
                System.out.println("  [DEBUG] Paramètres:");
                System.out.println("    valeur = " + valeur);
                System.out.println("    date = " + dateTimestamp);
                System.out.println("    service_type = '" + serviceType + "'");
                System.out.println("    zone = '" + zone + "'");
                
                System.out.println("  [DEBUG] Exécution de executeUpdate()...");
                int rowsAffected = pstmt.executeUpdate();
                System.out.println("  [DEBUG] Lignes affectées: " + rowsAffected);
                
                if (rowsAffected > 0) {
                    System.out.println("✓ Consommation insérée avec succès! (lignes affectées: " + rowsAffected + ")");
                    
                    ResultSet rs = pstmt.getGeneratedKeys();
                    if (rs.next()) {
                        long generatedId = rs.getLong(1);
                        consommation.setId(generatedId);
                        System.out.println("  ✓ ID généré: " + generatedId);
                        
                        // Vérifier immédiatement que la donnée est bien dans la base
                        try (Statement verifyStmt = conn.createStatement()) {
                            ResultSet verifyRs = verifyStmt.executeQuery("SELECT * FROM consommation WHERE id = " + generatedId);
                            if (verifyRs.next()) {
                                System.out.println("  ✓ VÉRIFICATION: La donnée est bien présente dans la base!");
                                System.out.println("    ID=" + verifyRs.getLong("id") + 
                                                 ", valeur=" + verifyRs.getDouble("valeur") + 
                                                 ", service_type='" + verifyRs.getString("service_type") + "'" +
                                                 ", zone='" + verifyRs.getString("zone") + "'");
                            } else {
                                System.err.println("  ✗ ATTENTION: La donnée n'a pas été trouvée après insertion!");
                            }
                        }
                    } else {
                        System.err.println("  ✗ ATTENTION: Aucun ID généré!");
                    }
                } else {
                    System.err.println("✗ ATTENTION: Aucune ligne affectée lors de l'insertion!");
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ ERREUR SQL lors de l'insertion de la consommation:");
            System.err.println("  Message: " + e.getMessage());
            System.err.println("  Code SQL: " + e.getSQLState());
            System.err.println("  Erreur MySQL: " + e.getErrorCode());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("✗ ERREUR inattendue lors de l'insertion:");
            e.printStackTrace();
        }
        System.out.println("=== [ConsommationDAO.insert] FIN ===\n");
    }

    public List<Consommation> findAll() {
        List<Consommation> consommations = new ArrayList<>();
        String sql = "SELECT * FROM consommation ORDER BY date DESC";
        try (Statement stmt = dbManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                consommations.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération: " + e.getMessage());
        }
        return consommations;
    }

    public Consommation findById(Long id) {
        String sql = "SELECT * FROM consommation WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche: " + e.getMessage());
        }
        return null;
    }

    public void update(Consommation consommation) {
        String sql = "UPDATE consommation SET valeur = ?, date = ?, service_type = ?, zone = ? WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setDouble(1, consommation.getValeur());
            pstmt.setTimestamp(2, Timestamp.valueOf(consommation.getDate()));
            pstmt.setString(3, consommation.getServiceType());
            pstmt.setString(4, consommation.getZone());
            pstmt.setLong(5, consommation.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM consommation WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression: " + e.getMessage());
        }
    }

    private Consommation mapResultSet(ResultSet rs) throws SQLException {
        Consommation consommation = new Consommation();
        consommation.setId(rs.getLong("id"));
        
        // Gestion des valeurs NULL pour les colonnes numériques
        Object valeurObj = rs.getObject("valeur");
        if (valeurObj != null) {
            consommation.setValeur(rs.getDouble("valeur"));
        } else {
            consommation.setValeur(0.0); // Valeur par défaut si null
        }
        
        // Gérer les valeurs NULL pour les chaînes
        String serviceType = rs.getString("service_type");
        consommation.setServiceType(serviceType != null ? serviceType : "");
        
        String zone = rs.getString("zone");
        consommation.setZone(zone != null ? zone : "");
        
        Timestamp ts = rs.getTimestamp("date");
        if (ts != null) {
            consommation.setDate(ts.toLocalDateTime());
        } else {
            consommation.setDate(java.time.LocalDateTime.now()); // Date par défaut si null
        }
        
        return consommation;
    }
}


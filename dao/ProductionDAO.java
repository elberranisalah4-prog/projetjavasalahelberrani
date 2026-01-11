package com.municipal.dashboard.dao;

import com.municipal.dashboard.DatabaseManager;
import com.municipal.dashboard.Production;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductionDAO {
    private DatabaseManager dbManager;

    public ProductionDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public void insert(Production production) {
        Connection conn = dbManager.getConnection();
        if (conn == null) {
            System.err.println("✗ ERREUR CRITIQUE: Connexion à la base de données est NULL!");
            return;
        }
        
        String sql = "INSERT INTO production (valeur, date, service_type, zone) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setDouble(1, production.getValeur() != null ? production.getValeur() : 0.0);
            if (production.getDate() != null) {
                pstmt.setTimestamp(2, Timestamp.valueOf(production.getDate()));
            } else {
                pstmt.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
            }
            pstmt.setString(3, production.getServiceType() != null ? production.getServiceType() : "");
            pstmt.setString(4, production.getZone() != null ? production.getZone() : "");
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✓ Production insérée avec succès! (lignes affectées: " + rowsAffected + ")");
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    production.setId(rs.getLong(1));
                    System.out.println("  ✓ ID généré: " + production.getId());
                }
            } else {
                System.err.println("✗ ATTENTION: Aucune ligne affectée lors de l'insertion!");
            }
        } catch (SQLException e) {
            System.err.println("✗ ERREUR SQL lors de l'insertion de la production: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Production> findAll() {
        List<Production> productions = new ArrayList<>();
        String sql = "SELECT * FROM production ORDER BY date DESC";
        try (Statement stmt = dbManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                productions.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération: " + e.getMessage());
        }
        return productions;
    }

    public Production findById(Long id) {
        String sql = "SELECT * FROM production WHERE id = ?";
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

    public void update(Production production) {
        String sql = "UPDATE production SET valeur = ?, date = ?, service_type = ?, zone = ? WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setDouble(1, production.getValeur());
            pstmt.setTimestamp(2, Timestamp.valueOf(production.getDate()));
            pstmt.setString(3, production.getServiceType());
            pstmt.setString(4, production.getZone());
            pstmt.setLong(5, production.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM production WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression: " + e.getMessage());
        }
    }

    private Production mapResultSet(ResultSet rs) throws SQLException {
        Production production = new Production();
        production.setId(rs.getLong("id"));
        
        // Gestion des valeurs NULL pour les colonnes numériques
        Object valeurObj = rs.getObject("valeur");
        if (valeurObj != null) {
            production.setValeur(rs.getDouble("valeur"));
        } else {
            production.setValeur(0.0); // Valeur par défaut si null
        }
        
        // Gérer les valeurs NULL pour les chaînes
        String serviceType = rs.getString("service_type");
        production.setServiceType(serviceType != null ? serviceType : "");
        
        String zone = rs.getString("zone");
        production.setZone(zone != null ? zone : "");
        
        Timestamp ts = rs.getTimestamp("date");
        if (ts != null) {
            production.setDate(ts.toLocalDateTime());
        } else {
            production.setDate(java.time.LocalDateTime.now()); // Date par défaut si null
        }
        
        return production;
    }
}


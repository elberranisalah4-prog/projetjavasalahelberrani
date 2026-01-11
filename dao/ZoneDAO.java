package com.municipal.dashboard.dao;

import com.municipal.dashboard.DatabaseManager;
import com.municipal.dashboard.Zone;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ZoneDAO {
    private DatabaseManager dbManager;

    public ZoneDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public void insert(Zone zone) {
        String sql = "INSERT INTO zone (nom, code, description) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, zone.getNom());
            pstmt.setString(2, zone.getCode());
            pstmt.setString(3, zone.getDescription());
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                zone.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion de la zone: " + e.getMessage());
        }
    }

    public List<Zone> findAll() {
        List<Zone> zones = new ArrayList<>();
        String sql = "SELECT * FROM zone ORDER BY nom";
        try (Statement stmt = dbManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                zones.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération: " + e.getMessage());
        }
        return zones;
    }

    public Zone findById(Long id) {
        String sql = "SELECT * FROM zone WHERE id = ?";
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

    public Zone findByCode(String code) {
        String sql = "SELECT * FROM zone WHERE code = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche: " + e.getMessage());
        }
        return null;
    }

    public void update(Zone zone) {
        String sql = "UPDATE zone SET nom = ?, code = ?, description = ? WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, zone.getNom());
            pstmt.setString(2, zone.getCode());
            pstmt.setString(3, zone.getDescription());
            pstmt.setLong(4, zone.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM zone WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression: " + e.getMessage());
        }
    }

    private Zone mapResultSet(ResultSet rs) throws SQLException {
        Zone zone = new Zone();
        zone.setId(rs.getLong("id"));
        // Gérer les valeurs NULL - utiliser une chaîne vide au lieu de null
        String nom = rs.getString("nom");
        zone.setNom(nom != null ? nom : "");
        String code = rs.getString("code");
        zone.setCode(code != null ? code : "");
        String description = rs.getString("description");
        zone.setDescription(description != null ? description : "");
        return zone;
    }
}


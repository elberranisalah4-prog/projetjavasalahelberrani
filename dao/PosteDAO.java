package com.municipal.dashboard.dao;

import com.municipal.dashboard.DatabaseManager;
import com.municipal.dashboard.Poste;

import java.sql.*;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class PosteDAO {
    private DatabaseManager dbManager;

    public PosteDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public void insert(Poste poste) {
        Connection conn = dbManager.getConnection();
        if (conn == null) {
            System.err.println("✗ ERREUR CRITIQUE: Connexion à la base de données est NULL!");
            return;
        }
        
        String sql = "INSERT INTO poste (titre, type, description, departement, niveau, salaire_min, salaire_max) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, poste.getTitre() != null ? poste.getTitre() : "");
            pstmt.setString(2, poste.getType());
            pstmt.setString(3, poste.getDescription());
            pstmt.setString(4, poste.getDepartement());
            pstmt.setString(5, poste.getNiveau());
            if (poste.getSalaireMin() != null) {
                pstmt.setDouble(6, poste.getSalaireMin());
            } else {
                pstmt.setNull(6, Types.DOUBLE);
            }
            if (poste.getSalaireMax() != null) {
                pstmt.setDouble(7, poste.getSalaireMax());
            } else {
                pstmt.setNull(7, Types.DOUBLE);
            }
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✓ Poste inséré avec succès! (lignes affectées: " + rowsAffected + ")");
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    poste.setId(rs.getLong(1));
                    System.out.println("  ✓ ID généré: " + poste.getId());
                }
            } else {
                System.err.println("✗ ATTENTION: Aucune ligne affectée lors de l'insertion!");
            }
        } catch (SQLException e) {
            System.err.println("✗ ERREUR SQL lors de l'insertion du poste: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Poste> findAll() {
        List<Poste> postes = new ArrayList<>();
        String sql = "SELECT * FROM poste ORDER BY titre";
        try (Statement stmt = dbManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                postes.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération: " + e.getMessage());
        }
        return postes;
    }

    public Poste findById(Long id) {
        String sql = "SELECT * FROM poste WHERE id = ?";
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

    public void update(Poste poste) {
        String sql = "UPDATE poste SET titre = ?, type = ?, description = ?, departement = ?, " +
                     "niveau = ?, salaire_min = ?, salaire_max = ? WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, poste.getTitre());
            pstmt.setString(2, poste.getType());
            pstmt.setString(3, poste.getDescription());
            pstmt.setString(4, poste.getDepartement());
            pstmt.setString(5, poste.getNiveau());
            pstmt.setDouble(6, poste.getSalaireMin());
            pstmt.setDouble(7, poste.getSalaireMax());
            pstmt.setLong(8, poste.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM poste WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression: " + e.getMessage());
        }
    }

    private Poste mapResultSet(ResultSet rs) throws SQLException {
        Poste poste = new Poste();
        poste.setId(rs.getLong("id"));
        poste.setTitre(rs.getString("titre"));
        poste.setType(rs.getString("type"));
        poste.setDescription(rs.getString("description"));
        poste.setDepartement(rs.getString("departement"));
        poste.setNiveau(rs.getString("niveau"));
        
        // Gestion des valeurs NULL pour les colonnes numériques
        Object salaireMinObj = rs.getObject("salaire_min");
        if (salaireMinObj != null) {
            poste.setSalaireMin(rs.getDouble("salaire_min"));
        }
        
        Object salaireMaxObj = rs.getObject("salaire_max");
        if (salaireMaxObj != null) {
            poste.setSalaireMax(rs.getDouble("salaire_max"));
        }
        
        return poste;
    }
}


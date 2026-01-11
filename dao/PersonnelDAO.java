package com.municipal.dashboard.dao;

import com.municipal.dashboard.DatabaseManager;
import com.municipal.dashboard.Personnel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonnelDAO {
    private DatabaseManager dbManager;

    public PersonnelDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public void insert(Personnel personnel) {
        Connection conn = dbManager.getConnection();
        if (conn == null) {
            System.err.println("✗ ERREUR CRITIQUE: Connexion à la base de données est NULL!");
            return;
        }
        
        String sql = "INSERT INTO personnel (nom, prenom, email, telephone, poste, departement, date_embauche, statut, matricule) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, personnel.getNom() != null ? personnel.getNom() : "");
            pstmt.setString(2, personnel.getPrenom() != null ? personnel.getPrenom() : "");
            pstmt.setString(3, personnel.getEmail() != null ? personnel.getEmail() : "");
            pstmt.setString(4, personnel.getTelephone());
            pstmt.setString(5, personnel.getPoste());
            pstmt.setString(6, personnel.getDepartement());
            if (personnel.getDateEmbauche() != null) {
                pstmt.setTimestamp(7, Timestamp.valueOf(personnel.getDateEmbauche()));
            } else {
                pstmt.setTimestamp(7, Timestamp.valueOf(java.time.LocalDateTime.now()));
            }
            pstmt.setString(8, personnel.getStatut());
            pstmt.setString(9, personnel.getMatricule());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✓ Personnel inséré avec succès! (lignes affectées: " + rowsAffected + ")");
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    personnel.setId(rs.getLong(1));
                    System.out.println("  ✓ ID généré: " + personnel.getId());
                }
            } else {
                System.err.println("✗ ATTENTION: Aucune ligne affectée lors de l'insertion!");
            }
        } catch (SQLException e) {
            System.err.println("✗ ERREUR SQL lors de l'insertion du personnel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Personnel> findAll() {
        List<Personnel> personnels = new ArrayList<>();
        String sql = "SELECT * FROM personnel ORDER BY nom, prenom";
        try (Statement stmt = dbManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                personnels.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération: " + e.getMessage());
        }
        return personnels;
    }

    public Personnel findById(Long id) {
        String sql = "SELECT * FROM personnel WHERE id = ?";
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

    public List<Personnel> findByDepartement(String departement) {
        List<Personnel> personnels = new ArrayList<>();
        String sql = "SELECT * FROM personnel WHERE departement = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, departement);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                personnels.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche: " + e.getMessage());
        }
        return personnels;
    }

    public void update(Personnel personnel) {
        String sql = "UPDATE personnel SET nom = ?, prenom = ?, email = ?, telephone = ?, poste = ?, " +
                     "departement = ?, date_embauche = ?, statut = ?, matricule = ? WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, personnel.getNom());
            pstmt.setString(2, personnel.getPrenom());
            pstmt.setString(3, personnel.getEmail());
            pstmt.setString(4, personnel.getTelephone());
            pstmt.setString(5, personnel.getPoste());
            pstmt.setString(6, personnel.getDepartement());
            pstmt.setTimestamp(7, Timestamp.valueOf(personnel.getDateEmbauche()));
            pstmt.setString(8, personnel.getStatut());
            pstmt.setString(9, personnel.getMatricule());
            pstmt.setLong(10, personnel.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM personnel WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression: " + e.getMessage());
        }
    }

    private Personnel mapResultSet(ResultSet rs) throws SQLException {
        Personnel personnel = new Personnel();
        personnel.setId(rs.getLong("id"));
        personnel.setNom(rs.getString("nom"));
        personnel.setPrenom(rs.getString("prenom"));
        personnel.setEmail(rs.getString("email"));
        personnel.setTelephone(rs.getString("telephone"));
        personnel.setPoste(rs.getString("poste"));
        personnel.setDepartement(rs.getString("departement"));
        personnel.setStatut(rs.getString("statut"));
        personnel.setMatricule(rs.getString("matricule"));
        
        Timestamp ts = rs.getTimestamp("date_embauche");
        if (ts != null) {
            personnel.setDateEmbauche(ts.toLocalDateTime());
        }
        
        return personnel;
    }
}


package com.municipal.dashboard.dao;

import com.municipal.dashboard.Admin;
import com.municipal.dashboard.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    private DatabaseManager dbManager;

    public AdminDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public void insert(Admin admin) throws SQLException {
        if (dbManager.getConnection() == null) {
            throw new SQLException("Aucune connexion à la base de données disponible");
        }
        String sql = "INSERT INTO admin (nom, prenom, email, mot_de_passe, role, telephone, statut, date_creation) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, admin.getNom());
            pstmt.setString(2, admin.getPrenom());
            pstmt.setString(3, admin.getEmail());
            pstmt.setString(4, admin.getMotDePasse());
            pstmt.setString(5, admin.getRole());
            pstmt.setString(6, admin.getTelephone());
            pstmt.setString(7, admin.getStatut());
            pstmt.setTimestamp(8, Timestamp.valueOf(admin.getDateCreation()));
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                admin.setId(rs.getLong(1));
            }
        }
    }

    public List<Admin> findAll() {
        List<Admin> admins = new ArrayList<>();
        if (dbManager.getConnection() == null) {
            return admins;
        }
        String sql = "SELECT * FROM admin ORDER BY nom, prenom";
        try (Statement stmt = dbManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                admins.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération: " + e.getMessage());
        }
        return admins;
    }

    public Admin findById(Long id) {
        String sql = "SELECT * FROM admin WHERE id = ?";
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

    public Admin findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        // Comparaison insensible à la casse pour l'email
        String sql = "SELECT * FROM admin WHERE LOWER(email) = LOWER(?)";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, email.trim());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par email: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void update(Admin admin) {
        String sql = "UPDATE admin SET nom = ?, prenom = ?, email = ?, role = ?, telephone = ?, statut = ? WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, admin.getNom());
            pstmt.setString(2, admin.getPrenom());
            pstmt.setString(3, admin.getEmail());
            pstmt.setString(4, admin.getRole());
            pstmt.setString(5, admin.getTelephone());
            pstmt.setString(6, admin.getStatut());
            pstmt.setLong(7, admin.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }

    public void updateLastConnection(Long id) {
        String sql = "UPDATE admin SET derniere_connexion = ? WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(java.time.LocalDateTime.now()));
            pstmt.setLong(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM admin WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression: " + e.getMessage());
        }
    }

    private Admin mapResultSet(ResultSet rs) throws SQLException {
        Admin admin = new Admin();
        admin.setId(rs.getLong("id"));
        admin.setNom(rs.getString("nom"));
        admin.setPrenom(rs.getString("prenom"));
        admin.setEmail(rs.getString("email"));
        admin.setMotDePasse(rs.getString("mot_de_passe"));
        admin.setRole(rs.getString("role"));
        admin.setTelephone(rs.getString("telephone"));
        admin.setStatut(rs.getString("statut"));
        
        Timestamp ts = rs.getTimestamp("date_creation");
        if (ts != null) {
            admin.setDateCreation(ts.toLocalDateTime());
        }
        
        ts = rs.getTimestamp("derniere_connexion");
        if (ts != null) {
            admin.setDerniereConnexion(ts.toLocalDateTime());
        }
        
        return admin;
    }
}


package com.municipal.dashboard.dao;

import com.municipal.dashboard.DatabaseManager;
import com.municipal.dashboard.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {
    private DatabaseManager dbManager;

    public ServiceDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public void insert(Service service) {
        String sql = "INSERT INTO service (type, nom, description) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, service.getType());
            pstmt.setString(2, service.getNom());
            pstmt.setString(3, service.getDescription());
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                service.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion du service: " + e.getMessage());
        }
    }

    public List<Service> findAll() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM service ORDER BY nom";
        try (Statement stmt = dbManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                services.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération: " + e.getMessage());
        }
        return services;
    }

    public Service findById(Long id) {
        String sql = "SELECT * FROM service WHERE id = ?";
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

    public List<Service> findByType(String type) {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM service WHERE type = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, type);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                services.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche: " + e.getMessage());
        }
        return services;
    }

    public void update(Service service) {
        String sql = "UPDATE service SET type = ?, nom = ?, description = ? WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, service.getType());
            pstmt.setString(2, service.getNom());
            pstmt.setString(3, service.getDescription());
            pstmt.setLong(4, service.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM service WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression: " + e.getMessage());
        }
    }

    private Service mapResultSet(ResultSet rs) throws SQLException {
        Service service = new Service();
        service.setId(rs.getLong("id"));
        service.setType(rs.getString("type"));
        service.setNom(rs.getString("nom"));
        service.setDescription(rs.getString("description"));
        return service;
    }
}


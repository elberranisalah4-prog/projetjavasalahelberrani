package com.municipal.dashboard.repository;

import com.municipal.dashboard.DatabaseManager;
import com.municipal.dashboard.model.Produit;
import com.municipal.dashboard.exception.StockException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du repository pour les produits
 * Utilise JDBC pour la persistance
 */
public class ProduitRepository implements Repository<Produit, Long> {
    
    private DatabaseManager dbManager;
    
    public ProduitRepository() {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    @Override
    public Produit save(Produit produit) {
        String sql = "INSERT INTO produit (nom, type, valeur, date, zone, description) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, produit.getNom());
            pstmt.setString(2, produit.getType());
            pstmt.setDouble(3, produit.getValeur() != null ? produit.getValeur() : 0.0);
            pstmt.setTimestamp(4, Timestamp.valueOf(
                produit.getDate() != null ? produit.getDate() : LocalDateTime.now()
            ));
            pstmt.setString(5, produit.getZone());
            pstmt.setString(6, produit.getDescription());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    produit.setId(rs.getLong(1));
                }
            }
            
            return produit;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Produit> findById(Long id) {
        String sql = "SELECT * FROM produit WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSet(rs));
            }
            
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Produit> findAll() {
        String sql = "SELECT * FROM produit ORDER BY date DESC";
        List<Produit> produits = new ArrayList<>();
        
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                produits.add(mapResultSet(rs));
            }
            
            return produits;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Produit update(Produit produit) {
        String sql = "UPDATE produit SET nom = ?, type = ?, valeur = ?, date = ?, zone = ?, description = ? " +
                     "WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, produit.getNom());
            pstmt.setString(2, produit.getType());
            pstmt.setDouble(3, produit.getValeur() != null ? produit.getValeur() : 0.0);
            pstmt.setTimestamp(4, Timestamp.valueOf(
                produit.getDate() != null ? produit.getDate() : LocalDateTime.now()
            ));
            pstmt.setString(5, produit.getZone());
            pstmt.setString(6, produit.getDescription());
            pstmt.setLong(7, produit.getId());
            
            pstmt.executeUpdate();
            return produit;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM produit WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression: " + e.getMessage(), e);
        }
    }
    
    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM produit";
        
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du comptage: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }
    
    private Produit mapResultSet(ResultSet rs) throws SQLException {
        Produit produit = new Produit();
        produit.setId(rs.getLong("id"));
        produit.setNom(rs.getString("nom"));
        produit.setType(rs.getString("type"));
        produit.setValeur(rs.getDouble("valeur"));
        
        Timestamp ts = rs.getTimestamp("date");
        if (ts != null) {
            produit.setDate(ts.toLocalDateTime());
        }
        
        produit.setZone(rs.getString("zone"));
        produit.setDescription(rs.getString("description"));
        
        return produit;
    }
}


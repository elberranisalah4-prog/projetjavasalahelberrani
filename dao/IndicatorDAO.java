package com.municipal.dashboard.dao;

import com.municipal.dashboard.DatabaseManager;
import com.municipal.dashboard.Indicator;

import java.sql.*;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class IndicatorDAO {
    private DatabaseManager dbManager;

    public IndicatorDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public void insert(Indicator indicator) {
        Connection conn = dbManager.getConnection();
        if (conn == null) {
            System.err.println("✗ ERREUR CRITIQUE: Connexion à la base de données est NULL!");
            return;
        }
        
        String sql = "INSERT INTO indicator (nom, type, valeur, valeur_cible, date_calcul, unite, description, statut) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, indicator.getNom() != null ? indicator.getNom() : "");
            pstmt.setString(2, indicator.getType());
            if (indicator.getValeur() != null) {
                pstmt.setDouble(3, indicator.getValeur());
            } else {
                pstmt.setNull(3, Types.DOUBLE);
            }
            if (indicator.getValeurCible() != null) {
                pstmt.setDouble(4, indicator.getValeurCible());
            } else {
                pstmt.setNull(4, Types.DOUBLE);
            }
            if (indicator.getDateCalcul() != null) {
                pstmt.setTimestamp(5, Timestamp.valueOf(indicator.getDateCalcul()));
            } else {
                pstmt.setTimestamp(5, Timestamp.valueOf(java.time.LocalDateTime.now()));
            }
            pstmt.setString(6, indicator.getUnite());
            pstmt.setString(7, indicator.getDescription());
            pstmt.setString(8, indicator.getStatut());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✓ Indicateur inséré avec succès! (lignes affectées: " + rowsAffected + ")");
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    indicator.setId(rs.getLong(1));
                    System.out.println("  ✓ ID généré: " + indicator.getId());
                }
            } else {
                System.err.println("✗ ATTENTION: Aucune ligne affectée lors de l'insertion!");
            }
        } catch (SQLException e) {
            System.err.println("✗ ERREUR SQL lors de l'insertion de l'indicateur: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Indicator> findAll() {
        List<Indicator> indicators = new ArrayList<>();
        String sql = "SELECT * FROM indicator ORDER BY date_calcul DESC";
        try (Statement stmt = dbManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                indicators.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération: " + e.getMessage());
        }
        return indicators;
    }

    public Indicator findById(Long id) {
        String sql = "SELECT * FROM indicator WHERE id = ?";
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

    public void update(Indicator indicator) {
        String sql = "UPDATE indicator SET nom = ?, type = ?, valeur = ?, valeur_cible = ?, " +
                     "date_calcul = ?, unite = ?, description = ?, statut = ? WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, indicator.getNom());
            pstmt.setString(2, indicator.getType());
            if (indicator.getValeur() != null) {
                pstmt.setDouble(3, indicator.getValeur());
            } else {
                pstmt.setNull(3, Types.DOUBLE);
            }
            if (indicator.getValeurCible() != null) {
                pstmt.setDouble(4, indicator.getValeurCible());
            } else {
                pstmt.setNull(4, Types.DOUBLE);
            }
            if (indicator.getDateCalcul() != null) {
                pstmt.setTimestamp(5, Timestamp.valueOf(indicator.getDateCalcul()));
            } else {
                pstmt.setTimestamp(5, Timestamp.valueOf(java.time.LocalDateTime.now()));
            }
            pstmt.setString(6, indicator.getUnite());
            pstmt.setString(7, indicator.getDescription());
            pstmt.setString(8, indicator.getStatut());
            pstmt.setLong(9, indicator.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM indicator WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression: " + e.getMessage());
        }
    }

    private Indicator mapResultSet(ResultSet rs) throws SQLException {
        Indicator indicator = new Indicator();
        indicator.setId(rs.getLong("id"));
        indicator.setNom(rs.getString("nom"));
        indicator.setType(rs.getString("type"));
        
        // Gestion des valeurs NULL pour les colonnes numériques
        Object valeurObj = rs.getObject("valeur");
        if (valeurObj != null) {
            indicator.setValeur(rs.getDouble("valeur"));
        }
        
        Object valeurCibleObj = rs.getObject("valeur_cible");
        if (valeurCibleObj != null) {
            indicator.setValeurCible(rs.getDouble("valeur_cible"));
        }
        
        indicator.setUnite(rs.getString("unite"));
        indicator.setDescription(rs.getString("description"));
        indicator.setStatut(rs.getString("statut"));
        
        Timestamp ts = rs.getTimestamp("date_calcul");
        if (ts != null) {
            indicator.setDateCalcul(ts.toLocalDateTime());
        }
        
        return indicator;
    }
}


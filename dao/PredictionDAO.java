package com.municipal.dashboard.dao;

import com.municipal.dashboard.DatabaseManager;
import com.municipal.dashboard.Prediction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PredictionDAO {
    private DatabaseManager dbManager;

    public PredictionDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public void insert(Prediction prediction) {
        Connection conn = dbManager.getConnection();
        if (conn == null) {
            System.err.println("✗ ERREUR CRITIQUE: Connexion à la base de données est NULL!");
            return;
        }
        
        String sql = "INSERT INTO prediction (service_type, date_prediction, valeur_predite, confiance, zone, type_prediction, recommandation, date_calcul) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, prediction.getServiceType());
            if (prediction.getDatePrediction() != null) {
                pstmt.setTimestamp(2, Timestamp.valueOf(prediction.getDatePrediction()));
            } else {
                pstmt.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
            }
            if (prediction.getValeurPredite() != null) {
                pstmt.setDouble(3, prediction.getValeurPredite());
            } else {
                pstmt.setNull(3, Types.DOUBLE);
            }
            if (prediction.getConfiance() != null) {
                pstmt.setDouble(4, prediction.getConfiance());
            } else {
                pstmt.setNull(4, Types.DOUBLE);
            }
            pstmt.setString(5, prediction.getZone());
            pstmt.setString(6, prediction.getTypePrediction());
            pstmt.setString(7, prediction.getRecommandation());
            if (prediction.getDateCalcul() != null) {
                pstmt.setTimestamp(8, Timestamp.valueOf(prediction.getDateCalcul()));
            } else {
                pstmt.setTimestamp(8, Timestamp.valueOf(java.time.LocalDateTime.now()));
            }
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✓ Prédiction insérée avec succès! (lignes affectées: " + rowsAffected + ")");
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    prediction.setId(rs.getLong(1));
                    System.out.println("  ✓ ID généré: " + prediction.getId());
                }
            } else {
                System.err.println("✗ ATTENTION: Aucune ligne affectée lors de l'insertion!");
            }
        } catch (SQLException e) {
            System.err.println("✗ ERREUR SQL lors de l'insertion de la prédiction: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Prediction> findAll() {
        List<Prediction> predictions = new ArrayList<>();
        String sql = "SELECT * FROM prediction ORDER BY date_prediction DESC";
        try (Statement stmt = dbManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                predictions.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération: " + e.getMessage());
        }
        return predictions;
    }

    public Prediction findById(Long id) {
        String sql = "SELECT * FROM prediction WHERE id = ?";
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

    public void update(Prediction prediction) {
        String sql = "UPDATE prediction SET service_type = ?, date_prediction = ?, valeur_predite = ?, " +
                     "confiance = ?, zone = ?, type_prediction = ?, recommandation = ? WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, prediction.getServiceType());
            pstmt.setTimestamp(2, Timestamp.valueOf(prediction.getDatePrediction()));
            pstmt.setDouble(3, prediction.getValeurPredite());
            pstmt.setDouble(4, prediction.getConfiance());
            pstmt.setString(5, prediction.getZone());
            pstmt.setString(6, prediction.getTypePrediction());
            pstmt.setString(7, prediction.getRecommandation());
            pstmt.setLong(8, prediction.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM prediction WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression: " + e.getMessage());
        }
    }

    private Prediction mapResultSet(ResultSet rs) throws SQLException {
        Prediction prediction = new Prediction();
        prediction.setId(rs.getLong("id"));
        prediction.setServiceType(rs.getString("service_type"));
        
        // Gestion des valeurs NULL pour les colonnes numériques
        Object valeurPrediteObj = rs.getObject("valeur_predite");
        if (valeurPrediteObj != null) {
            prediction.setValeurPredite(rs.getDouble("valeur_predite"));
        }
        
        Object confianceObj = rs.getObject("confiance");
        if (confianceObj != null) {
            prediction.setConfiance(rs.getDouble("confiance"));
        }
        
        prediction.setZone(rs.getString("zone"));
        prediction.setTypePrediction(rs.getString("type_prediction"));
        prediction.setRecommandation(rs.getString("recommandation"));
        
        Timestamp ts = rs.getTimestamp("date_prediction");
        if (ts != null) {
            prediction.setDatePrediction(ts.toLocalDateTime());
        }
        
        ts = rs.getTimestamp("date_calcul");
        if (ts != null) {
            prediction.setDateCalcul(ts.toLocalDateTime());
        }
        
        return prediction;
    }
}


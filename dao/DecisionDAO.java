package com.municipal.dashboard.dao;

import com.municipal.dashboard.DatabaseManager;
import com.municipal.dashboard.Decision;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DecisionDAO {
    private DatabaseManager dbManager;

    public DecisionDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public void insert(Decision decision) {
        Connection conn = dbManager.getConnection();
        if (conn == null) {
            System.err.println("✗ ERREUR CRITIQUE: Connexion à la base de données est NULL!");
            return;
        }
        
        String sql = "INSERT INTO decision (titre, description, type, statut, date_decision, date_application, " +
                     "auteur, service_concerne, zone_concernee, justification) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, decision.getTitre() != null ? decision.getTitre() : "");
            pstmt.setString(2, decision.getDescription());
            pstmt.setString(3, decision.getType() != null ? decision.getType() : "");
            pstmt.setString(4, decision.getStatut() != null ? decision.getStatut() : "");
            if (decision.getDateDecision() != null) {
                pstmt.setTimestamp(5, Timestamp.valueOf(decision.getDateDecision()));
            } else {
                pstmt.setTimestamp(5, Timestamp.valueOf(java.time.LocalDateTime.now()));
            }
            if (decision.getDateApplication() != null) {
                pstmt.setTimestamp(6, Timestamp.valueOf(decision.getDateApplication()));
            } else {
                pstmt.setNull(6, Types.TIMESTAMP);
            }
            pstmt.setString(7, decision.getAuteur());
            pstmt.setString(8, decision.getServiceConcerne());
            pstmt.setString(9, decision.getZoneConcernee());
            pstmt.setString(10, decision.getJustification());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✓ Décision insérée avec succès! (lignes affectées: " + rowsAffected + ")");
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    decision.setId(rs.getLong(1));
                    System.out.println("  ✓ ID généré: " + decision.getId());
                }
            } else {
                System.err.println("✗ ATTENTION: Aucune ligne affectée lors de l'insertion!");
            }
        } catch (SQLException e) {
            System.err.println("✗ ERREUR SQL lors de l'insertion de la décision: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Decision> findAll() {
        List<Decision> decisions = new ArrayList<>();
        String sql = "SELECT * FROM decision ORDER BY date_decision DESC";
        try (Statement stmt = dbManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                decisions.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération: " + e.getMessage());
        }
        return decisions;
    }

    public Decision findById(Long id) {
        String sql = "SELECT * FROM decision WHERE id = ?";
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

    public void update(Decision decision) {
        String sql = "UPDATE decision SET titre = ?, description = ?, type = ?, statut = ?, date_decision = ?, " +
                     "date_application = ?, auteur = ?, service_concerne = ?, zone_concernee = ?, justification = ? WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, decision.getTitre());
            pstmt.setString(2, decision.getDescription());
            pstmt.setString(3, decision.getType());
            pstmt.setString(4, decision.getStatut());
            pstmt.setTimestamp(5, Timestamp.valueOf(decision.getDateDecision()));
            if (decision.getDateApplication() != null) {
                pstmt.setTimestamp(6, Timestamp.valueOf(decision.getDateApplication()));
            } else {
                pstmt.setNull(6, Types.TIMESTAMP);
            }
            pstmt.setString(7, decision.getAuteur());
            pstmt.setString(8, decision.getServiceConcerne());
            pstmt.setString(9, decision.getZoneConcernee());
            pstmt.setString(10, decision.getJustification());
            pstmt.setLong(11, decision.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM decision WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression: " + e.getMessage());
        }
    }

    private Decision mapResultSet(ResultSet rs) throws SQLException {
        Decision decision = new Decision();
        decision.setId(rs.getLong("id"));
        decision.setTitre(rs.getString("titre"));
        decision.setDescription(rs.getString("description"));
        decision.setType(rs.getString("type"));
        decision.setStatut(rs.getString("statut"));
        decision.setAuteur(rs.getString("auteur"));
        decision.setServiceConcerne(rs.getString("service_concerne"));
        decision.setZoneConcernee(rs.getString("zone_concernee"));
        decision.setJustification(rs.getString("justification"));
        
        Timestamp ts = rs.getTimestamp("date_decision");
        if (ts != null) {
            decision.setDateDecision(ts.toLocalDateTime());
        }
        
        ts = rs.getTimestamp("date_application");
        if (ts != null) {
            decision.setDateApplication(ts.toLocalDateTime());
        }
        
        return decision;
    }
}


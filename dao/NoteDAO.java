package com.municipal.dashboard.dao;

import com.municipal.dashboard.DatabaseManager;
import com.municipal.dashboard.Note;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoteDAO {
    private DatabaseManager dbManager;

    public NoteDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public void insert(Note note) {
        Connection conn = dbManager.getConnection();
        if (conn == null) {
            System.err.println("✗ ERREUR CRITIQUE: Connexion à la base de données est NULL!");
            return;
        }
        
        String sql = "INSERT INTO note (contenu, date_creation, auteur, type) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, note.getContenu() != null ? note.getContenu() : "");
            if (note.getDateCreation() != null) {
                pstmt.setTimestamp(2, Timestamp.valueOf(note.getDateCreation()));
            } else {
                pstmt.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
            }
            pstmt.setString(3, note.getAuteur());
            pstmt.setString(4, note.getType());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✓ Note insérée avec succès! (lignes affectées: " + rowsAffected + ")");
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    note.setId(rs.getLong(1));
                    System.out.println("  ✓ ID généré: " + note.getId());
                }
            } else {
                System.err.println("✗ ATTENTION: Aucune ligne affectée lors de l'insertion!");
            }
        } catch (SQLException e) {
            System.err.println("✗ ERREUR SQL lors de l'insertion de la note: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Note> findAll() {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT * FROM note ORDER BY date_creation DESC";
        try (Statement stmt = dbManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                notes.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération: " + e.getMessage());
        }
        return notes;
    }

    public Note findById(Long id) {
        String sql = "SELECT * FROM note WHERE id = ?";
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

    public void update(Note note) {
        String sql = "UPDATE note SET contenu = ?, auteur = ?, type = ? WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, note.getContenu());
            pstmt.setString(2, note.getAuteur());
            pstmt.setString(3, note.getType());
            pstmt.setLong(4, note.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM note WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression: " + e.getMessage());
        }
    }

    private Note mapResultSet(ResultSet rs) throws SQLException {
        Note note = new Note();
        note.setId(rs.getLong("id"));
        note.setContenu(rs.getString("contenu"));
        note.setAuteur(rs.getString("auteur"));
        note.setType(rs.getString("type"));
        
        Timestamp ts = rs.getTimestamp("date_creation");
        if (ts != null) {
            note.setDateCreation(ts.toLocalDateTime());
        }
        
        return note;
    }
}


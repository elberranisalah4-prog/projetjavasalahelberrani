package com.municipal.dashboard;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Database {
    
    public static class ServiceData {
        private Long id;
        private String serviceType;
        private LocalDateTime date;
        private Double consommation;
        private Double production;
        private Double cout;
        private String zone;
        private String notes;

        public ServiceData() {
        }

        public ServiceData(String serviceType, LocalDateTime date, Double consommation, 
                          Double production, Double cout, String zone) {
            this.serviceType = serviceType;
            this.date = date;
            this.consommation = consommation;
            this.production = production;
            this.cout = cout;
            this.zone = zone;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getServiceType() {
            return serviceType;
        }

        public void setServiceType(String serviceType) {
            this.serviceType = serviceType;
        }

        public LocalDateTime getDate() {
            return date;
        }

        public void setDate(LocalDateTime date) {
            this.date = date;
        }

        public Double getConsommation() {
            return consommation;
        }

        public void setConsommation(Double consommation) {
            this.consommation = consommation;
        }

        public Double getProduction() {
            return production;
        }

        public void setProduction(Double production) {
            this.production = production;
        }

        public Double getCout() {
            return cout;
        }

        public void setCout(Double cout) {
            this.cout = cout;
        }

        public String getZone() {
            return zone;
        }

        public void setZone(String zone) {
            this.zone = zone;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        @Override
        public String toString() {
            return "ServiceData{" +
                    "id=" + id +
                    ", serviceType='" + serviceType + '\'' +
                    ", date=" + date +
                    ", consommation=" + consommation +
                    ", production=" + production +
                    ", cout=" + cout +
                    ", zone='" + zone + '\'' +
                    '}';
        }
    }

    // Configuration MySQL pour XAMPP
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "services_publics";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME 
            + "?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true";
    private Connection connection;

    public Database() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            createTables();
            System.out.println("Base de données MySQL initialisée avec succès!");
        } catch (ClassNotFoundException e) {
            System.err.println("ERREUR: Driver MySQL non trouvé!");
            System.err.println("Téléchargez mysql-connector-java.jar et ajoutez-le au classpath");
            System.err.println("Lien: https://dev.mysql.com/downloads/connector/j/");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation de la base de données: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS service_data (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "service_type VARCHAR(50) NOT NULL, " +
                "date DATETIME NOT NULL, " +
                "consommation DOUBLE, " +
                "production DOUBLE, " +
                "cout DOUBLE, " +
                "zone VARCHAR(100), " +
                "notes TEXT" +
                ")";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table 'service_data' créée avec succès!");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void insert(ServiceData data) {
        String sql = "INSERT INTO service_data (service_type, date, consommation, production, cout, zone, notes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, data.getServiceType());
            pstmt.setTimestamp(2, Timestamp.valueOf(data.getDate()));
            pstmt.setDouble(3, data.getConsommation());
            pstmt.setDouble(4, data.getProduction());
            pstmt.setDouble(5, data.getCout());
            pstmt.setString(6, data.getZone());
            pstmt.setString(7, data.getNotes());
            pstmt.executeUpdate();
            System.out.println("Donnée insérée avec succès: " + data.getServiceType());
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<ServiceData> findAll() {
        List<ServiceData> results = new ArrayList<>();
        String sql = "SELECT * FROM service_data ORDER BY date DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ServiceData data = mapResultSetToServiceData(rs);
                results.add(data);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération: " + e.getMessage());
            e.printStackTrace();
        }
        
        return results;
    }

    public List<ServiceData> findByServiceType(String serviceType) {
        List<ServiceData> results = new ArrayList<>();
        String sql = "SELECT * FROM service_data WHERE service_type = ? ORDER BY date DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, serviceType);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ServiceData data = mapResultSetToServiceData(rs);
                results.add(data);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération: " + e.getMessage());
            e.printStackTrace();
        }
        
        return results;
    }

    public List<ServiceData> findByDateRange(LocalDateTime start, LocalDateTime end) {
        List<ServiceData> results = new ArrayList<>();
        String sql = "SELECT * FROM service_data WHERE date >= ? AND date <= ? ORDER BY date DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(start));
            pstmt.setTimestamp(2, Timestamp.valueOf(end));
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ServiceData data = mapResultSetToServiceData(rs);
                results.add(data);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération: " + e.getMessage());
            e.printStackTrace();
        }
        
        return results;
    }

    public List<ServiceData> findByZone(String zone) {
        List<ServiceData> results = new ArrayList<>();
        String sql = "SELECT * FROM service_data WHERE zone = ? ORDER BY date DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, zone);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ServiceData data = mapResultSetToServiceData(rs);
                results.add(data);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération: " + e.getMessage());
            e.printStackTrace();
        }
        
        return results;
    }

    public void update(ServiceData data) {
        String sql = "UPDATE service_data SET service_type = ?, date = ?, consommation = ?, " +
                     "production = ?, cout = ?, zone = ?, notes = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, data.getServiceType());
            pstmt.setTimestamp(2, Timestamp.valueOf(data.getDate()));
            pstmt.setDouble(3, data.getConsommation());
            pstmt.setDouble(4, data.getProduction());
            pstmt.setDouble(5, data.getCout());
            pstmt.setString(6, data.getZone());
            pstmt.setString(7, data.getNotes());
            pstmt.setLong(8, data.getId());
            pstmt.executeUpdate();
            System.out.println("Donnée mise à jour avec succès: ID " + data.getId());
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM service_data WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            System.out.println("Donnée supprimée avec succès: ID " + id);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM service_data";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }

    private ServiceData mapResultSetToServiceData(ResultSet rs) throws SQLException {
        ServiceData data = new ServiceData();
        data.setId(rs.getLong("id"));
        data.setServiceType(rs.getString("service_type"));
        
        Timestamp timestamp = rs.getTimestamp("date");
        if (timestamp != null) {
            data.setDate(timestamp.toLocalDateTime());
        }
        
        // Gestion des valeurs NULL pour les colonnes numériques
        Object consommationObj = rs.getObject("consommation");
        if (consommationObj != null) {
            data.setConsommation(rs.getDouble("consommation"));
        }
        
        Object productionObj = rs.getObject("production");
        if (productionObj != null) {
            data.setProduction(rs.getDouble("production"));
        }
        
        Object coutObj = rs.getObject("cout");
        if (coutObj != null) {
            data.setCout(rs.getDouble("cout"));
        }
        
        data.setZone(rs.getString("zone"));
        data.setNotes(rs.getString("notes"));
        
        return data;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion à la base de données fermée.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


package com.municipal.dashboard;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration sécurisée pour la base de données MySQL
 * Permet de stocker les identifiants de manière sécurisée
 */
public class DatabaseConfig {
    private static final String CONFIG_FILE = "db_config.properties";
    private static Properties properties;
    
    // Valeurs par défaut pour XAMPP (développement)
    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_PORT = "3306";
    private static final String DEFAULT_DB_NAME = "services_publics";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";
    
    static {
        loadConfig();
    }
    
    /**
     * Charge la configuration depuis le fichier properties
     * Crée le fichier avec les valeurs par défaut si il n'existe pas
     */
    private static void loadConfig() {
        properties = new Properties();
        
        try {
            // Essayer de charger le fichier de configuration
            FileInputStream fis = new FileInputStream(CONFIG_FILE);
            properties.load(fis);
            fis.close();
            System.out.println("✓ Configuration chargée depuis " + CONFIG_FILE);
        } catch (IOException e) {
            // Si le fichier n'existe pas, créer avec les valeurs par défaut
            System.out.println("⚠ Fichier de configuration non trouvé. Création avec valeurs par défaut...");
            createDefaultConfig();
        }
    }
    
    /**
     * Crée un fichier de configuration par défaut
     */
    private static void createDefaultConfig() {
        properties.setProperty("db.host", DEFAULT_HOST);
        properties.setProperty("db.port", DEFAULT_PORT);
        properties.setProperty("db.name", DEFAULT_DB_NAME);
        properties.setProperty("db.user", DEFAULT_USER);
        properties.setProperty("db.password", DEFAULT_PASSWORD);
        properties.setProperty("db.useSSL", "false");
        properties.setProperty("db.timezone", "UTC");
        
        saveConfig();
    }
    
    /**
     * Sauvegarde la configuration dans le fichier
     */
    public static void saveConfig() {
        try {
            FileOutputStream fos = new FileOutputStream(CONFIG_FILE);
            properties.store(fos, "Configuration de la base de données MySQL - Dashboard Services Publics");
            fos.close();
            System.out.println("✓ Configuration sauvegardée dans " + CONFIG_FILE);
        } catch (IOException e) {
            System.err.println("✗ Erreur lors de la sauvegarde de la configuration: " + e.getMessage());
        }
    }
    
    /**
     * Obtient la valeur d'une propriété
     */
    private static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    // Getters pour les propriétés de configuration
    public static String getHost() {
        return getProperty("db.host", DEFAULT_HOST);
    }
    
    public static String getPort() {
        return getProperty("db.port", DEFAULT_PORT);
    }
    
    public static String getDatabaseName() {
        return getProperty("db.name", DEFAULT_DB_NAME);
    }
    
    public static String getUser() {
        return getProperty("db.user", DEFAULT_USER);
    }
    
    public static String getPassword() {
        return getProperty("db.password", DEFAULT_PASSWORD);
    }
    
    public static boolean useSSL() {
        return Boolean.parseBoolean(getProperty("db.useSSL", "false"));
    }
    
    public static String getTimezone() {
        return getProperty("db.timezone", "UTC");
    }
    
    /**
     * Construit l'URL de connexion JDBC
     */
    public static String getConnectionURL() {
        String host = getHost();
        String port = getPort();
        String dbName = getDatabaseName();
        boolean ssl = useSSL();
        String timezone = getTimezone();
        
        StringBuilder url = new StringBuilder();
        url.append("jdbc:mysql://").append(host).append(":").append(port).append("/").append(dbName);
        url.append("?useSSL=").append(ssl);
        url.append("&serverTimezone=").append(timezone);
        url.append("&useUnicode=true");
        url.append("&characterEncoding=UTF-8");
        url.append("&allowPublicKeyRetrieval=true");
        
        return url.toString();
    }
    
    /**
     * Met à jour le mot de passe dans la configuration
     */
    public static void setPassword(String password) {
        properties.setProperty("db.password", password);
        saveConfig();
    }
    
    /**
     * Met à jour l'utilisateur dans la configuration
     */
    public static void setUser(String user) {
        properties.setProperty("db.user", user);
        saveConfig();
    }
    
    /**
     * Met à jour le nom de la base de données
     */
    public static void setDatabaseName(String dbName) {
        properties.setProperty("db.name", dbName);
        saveConfig();
    }
    
    /**
     * Active ou désactive SSL
     */
    public static void setUseSSL(boolean useSSL) {
        properties.setProperty("db.useSSL", String.valueOf(useSSL));
        saveConfig();
    }
}



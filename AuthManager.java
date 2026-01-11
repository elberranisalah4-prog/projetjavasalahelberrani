package com.municipal.dashboard;

import com.municipal.dashboard.dao.AdminDAO;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthManager {
    private static AuthManager instance;
    private AdminDAO adminDAO;
    private Map<String, Long> sessions; // token -> adminId
    
    private AuthManager() {
        this.adminDAO = new AdminDAO();
        this.sessions = new HashMap<>();
    }
    
    public static AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }
    
    /**
     * Vérifie s'il existe déjà un administrateur dans la base de données
     */
    public boolean hasAdmin() {
        try {
            java.util.List<Admin> admins = adminDAO.findAll();
            return admins != null && !admins.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Crée un nouvel administrateur. Retourne false si un admin existe déjà ou en cas d'erreur.
     */
    public boolean createAdmin(String nom, String prenom, String email, String password, String telephone) {
        if (hasAdmin()) {
            return false; // Un admin existe déjà
        }
        
        try {
            Admin admin = new Admin(
                nom,
                prenom,
                email,
                password,
                "SUPER_ADMIN",
                telephone
            );
            adminDAO.insert(admin);
            return true;
        } catch (Exception e) {
            System.err.println("Erreur lors de la création de l'admin: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public String login(String email, String password) {
        // Nettoyer les entrées (supprimer les espaces)
        if (email == null || password == null) {
            System.err.println("Tentative de connexion avec email ou mot de passe null");
            return null;
        }
        
        email = email.trim();
        password = password.trim();
        
        if (email.isEmpty() || password.isEmpty()) {
            System.err.println("Tentative de connexion avec email ou mot de passe vide");
            return null;
        }
        
        // Rechercher l'admin par email (comparaison insensible à la casse)
        Admin admin = adminDAO.findByEmail(email);
        
        if (admin == null) {
            System.err.println("Aucun admin trouvé avec l'email: " + email);
            return null;
        }
        
        // Vérifier le statut
        if (!"ACTIF".equals(admin.getStatut())) {
            System.err.println("Admin trouvé mais statut non ACTIF: " + admin.getStatut());
            return null;
        }
        
        // Vérifier le mot de passe
        String storedPassword = admin.getMotDePasse();
        if (storedPassword == null) {
            System.err.println("Mot de passe stocké est null pour l'admin: " + email);
            return null;
        }
        
        if (!storedPassword.equals(password)) {
            System.err.println("Mot de passe incorrect pour l'email: " + email);
            return null;
        }
        
        // Créer une session
        String token = UUID.randomUUID().toString();
        sessions.put(token, admin.getId());
        adminDAO.updateLastConnection(admin.getId());
        System.out.println("Connexion réussie pour l'admin: " + email);
        return token;
    }
    
    public void logout(String token) {
        sessions.remove(token);
    }
    
    public boolean isAuthenticated(String token) {
        return token != null && sessions.containsKey(token);
    }
    
    public Long getAdminId(String token) {
        return sessions.get(token);
    }
    
    public Admin getAdmin(String token) {
        Long adminId = getAdminId(token);
        if (adminId != null) {
            return adminDAO.findById(adminId);
        }
        return null;
    }
    
    /**
     * Retourne le AdminDAO pour les vérifications
     */
    public AdminDAO getAdminDAO() {
        return adminDAO;
    }
    
    /**
     * Supprime tous les administrateurs de la base de données
     */
    public boolean deleteAllAdmins() {
        try {
            java.util.List<Admin> admins = adminDAO.findAll();
            for (Admin admin : admins) {
                adminDAO.delete(admin.getId());
            }
            // Vider toutes les sessions
            sessions.clear();
            return true;
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression des admins: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}


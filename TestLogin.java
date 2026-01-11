package com.municipal.dashboard;

import com.municipal.dashboard.dao.AdminDAO;
import java.util.List;

public class TestLogin {
    
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("    TEST DE CONNEXION - DEBUG");
        System.out.println("=".repeat(60));
        System.out.println();
        
        // Initialisation
        DatabaseManager dbManager = DatabaseManager.getInstance();
        AdminDAO adminDAO = new AdminDAO();
        AuthManager authManager = AuthManager.getInstance();
        
        // Lister tous les admins
        List<Admin> admins = adminDAO.findAll();
        
        if (admins.isEmpty()) {
            System.out.println("❌ Aucun administrateur trouvé dans la base de données.");
            System.out.println("   Créez un admin via http://localhost:8080/create-admin");
            return;
        }
        
        System.out.println("📋 Administrateurs trouvés dans la base de données:");
        System.out.println();
        
        for (Admin admin : admins) {
            System.out.println("  ID: " + admin.getId());
            System.out.println("  Nom: " + admin.getNom() + " " + admin.getPrenom());
            System.out.println("  Email: " + admin.getEmail());
            System.out.println("  Email (lowercase): " + (admin.getEmail() != null ? admin.getEmail().toLowerCase() : "null"));
            System.out.println("  Statut: " + admin.getStatut());
            System.out.println("  Mot de passe stocké: " + (admin.getMotDePasse() != null ? 
                "'" + admin.getMotDePasse() + "' (longueur: " + admin.getMotDePasse().length() + ")" : "NULL"));
            System.out.println();
        }
        
        // Tester la connexion si un email est fourni en argument
        if (args.length >= 2) {
            String email = args[0];
            String password = args[1];
            
            System.out.println("🔍 Test de connexion:");
            System.out.println("  Email fourni: '" + email + "'");
            System.out.println("  Email (trim): '" + email.trim() + "'");
            System.out.println("  Email (lowercase): '" + email.trim().toLowerCase() + "'");
            System.out.println("  Mot de passe fourni: '" + password + "' (longueur: " + password.length() + ")");
            System.out.println();
            
            // Rechercher l'admin
            Admin admin = adminDAO.findByEmail(email);
            if (admin == null) {
                System.out.println("❌ Aucun admin trouvé avec cet email!");
            } else {
                System.out.println("✓ Admin trouvé:");
                System.out.println("  Email en DB: '" + admin.getEmail() + "'");
                System.out.println("  Mot de passe en DB: '" + admin.getMotDePasse() + "' (longueur: " + 
                    (admin.getMotDePasse() != null ? admin.getMotDePasse().length() : 0) + ")");
                System.out.println("  Statut: " + admin.getStatut());
                System.out.println();
                
                // Comparaison
                boolean emailMatch = admin.getEmail() != null && 
                    admin.getEmail().equalsIgnoreCase(email.trim());
                boolean passwordMatch = admin.getMotDePasse() != null && 
                    admin.getMotDePasse().equals(password);
                boolean statusOk = "ACTIF".equals(admin.getStatut());
                
                System.out.println("🔍 Vérifications:");
                System.out.println("  Email correspond: " + emailMatch);
                System.out.println("  Mot de passe correspond: " + passwordMatch);
                System.out.println("  Statut ACTIF: " + statusOk);
                System.out.println();
                
                // Test de connexion
                String token = authManager.login(email, password);
                if (token != null) {
                    System.out.println("✅ CONNEXION RÉUSSIE! Token: " + token);
                } else {
                    System.out.println("❌ CONNEXION ÉCHOUÉE!");
                    if (!emailMatch) {
                        System.out.println("   Raison: Email ne correspond pas");
                    }
                    if (!passwordMatch) {
                        System.out.println("   Raison: Mot de passe ne correspond pas");
                    }
                    if (!statusOk) {
                        System.out.println("   Raison: Statut n'est pas ACTIF");
                    }
                }
            }
        } else {
            System.out.println("💡 Pour tester une connexion, utilisez:");
            System.out.println("   java TestLogin <email> <mot_de_passe>");
        }
        
        // Fermeture
        if (dbManager != null) {
            dbManager.close();
        }
    }
}


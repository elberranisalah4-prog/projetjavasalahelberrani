package com.municipal.dashboard;

import com.municipal.dashboard.dao.AdminDAO;
import java.util.List;

public class FixAdmin {
    
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("    CORRECTION DES ADMINISTRATEURS");
        System.out.println("=".repeat(60));
        System.out.println();
        
        // Initialisation
        DatabaseManager dbManager = DatabaseManager.getInstance();
        AdminDAO adminDAO = new AdminDAO();
        
        // Lister tous les admins
        List<Admin> admins = adminDAO.findAll();
        
        if (admins.isEmpty()) {
            System.out.println("ℹ Aucun administrateur trouvé.");
            System.out.println("   Vous pouvez créer un admin via http://localhost:8080/create-admin");
            return;
        }
        
        System.out.println("📋 Administrateurs trouvés:");
        for (Admin admin : admins) {
            System.out.println("  ID: " + admin.getId());
            System.out.println("  Email: '" + admin.getEmail() + "'");
            System.out.println("  Nom: '" + admin.getNom() + "'");
            System.out.println("  Mot de passe: " + (admin.getMotDePasse() != null && !admin.getMotDePasse().isEmpty() ? 
                "présent (longueur: " + admin.getMotDePasse().length() + ")" : "VIDE ou NULL"));
            System.out.println();
        }
        
        // Supprimer les admins invalides (email vide ou mot de passe vide)
        System.out.println("🔧 Suppression des administrateurs invalides...");
        int deleted = 0;
        for (Admin admin : admins) {
            boolean invalid = (admin.getEmail() == null || admin.getEmail().trim().isEmpty()) ||
                             (admin.getMotDePasse() == null || admin.getMotDePasse().trim().isEmpty());
            
            if (invalid) {
                System.out.println("  Suppression de l'admin ID " + admin.getId() + " (données invalides)");
                adminDAO.delete(admin.getId());
                deleted++;
            }
        }
        
        if (deleted > 0) {
            System.out.println("✓ " + deleted + " administrateur(s) invalide(s) supprimé(s).");
            System.out.println();
            System.out.println("✅ Vous pouvez maintenant créer un nouvel administrateur:");
            System.out.println("   1. Allez sur http://localhost:8080/create-admin");
            System.out.println("   2. Remplissez tous les champs (nom, prénom, email, mot de passe)");
            System.out.println("   3. Cliquez sur 'Créer l'administrateur'");
        } else {
            System.out.println("ℹ Tous les administrateurs semblent valides.");
            System.out.println();
            System.out.println("Si vous ne pouvez toujours pas vous connecter:");
            System.out.println("  1. Vérifiez que vous utilisez le bon email (insensible à la casse)");
            System.out.println("  2. Vérifiez que le mot de passe correspond exactement");
            System.out.println("  3. Vérifiez que le statut est 'ACTIF'");
        }
        
        // Fermeture
        if (dbManager != null) {
            dbManager.close();
        }
    }
}


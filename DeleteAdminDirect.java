package com.municipal.dashboard;

import com.municipal.dashboard.dao.AdminDAO;
import java.util.List;

public class DeleteAdminDirect {
    
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("    SUPPRESSION D'ADMINISTRATEUR");
        System.out.println("    Dashboard Services Publics");
        System.out.println("=".repeat(60));
        System.out.println();
        
        // Initialisation de la base de données
        DatabaseManager dbManager = DatabaseManager.getInstance();
        AdminDAO adminDAO = new AdminDAO();
        
        // Récupérer tous les admins
        List<Admin> admins = adminDAO.findAll();
        
        if (admins.isEmpty()) {
            System.out.println("ℹ Aucun administrateur trouvé dans la base de données.");
            return;
        }
        
        System.out.println("Administrateurs trouvés:");
        System.out.println();
        for (Admin admin : admins) {
            System.out.println("  ID: " + admin.getId());
            System.out.println("  Nom: " + admin.getNom() + " " + admin.getPrenom());
            System.out.println("  Email: " + admin.getEmail());
            System.out.println("  Statut: " + admin.getStatut());
            System.out.println();
        }
        
        System.out.println("Suppression en cours...");
        System.out.println();
        
        // Supprimer tous les admins
        for (Admin admin : admins) {
            adminDAO.delete(admin.getId());
            System.out.println("✓ Administrateur supprimé: " + admin.getEmail());
        }
        
        System.out.println();
        System.out.println("✓ Tous les administrateurs ont été supprimés!");
        System.out.println("  Vous pouvez maintenant créer un nouvel administrateur.");
    }
}


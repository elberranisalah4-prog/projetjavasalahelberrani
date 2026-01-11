package com.municipal.dashboard;

import com.municipal.dashboard.dao.AdminDAO;
import java.util.List;

public class ListAdmins {
    
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("    LISTE DES ADMINISTRATEURS");
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
            System.out.println();
            System.out.println("Pour créer un administrateur:");
            System.out.println("  1. Utilisez le script CREER_ADMIN.bat ou CREER_ADMIN.ps1");
            System.out.println("  2. Ou accédez à http://localhost:8081/create-admin");
        } else {
            System.out.println("Administrateurs trouvés:");
            System.out.println();
            for (Admin admin : admins) {
                System.out.println("  ID: " + admin.getId());
                System.out.println("  Nom: " + admin.getNom() + " " + admin.getPrenom());
                System.out.println("  Email: " + admin.getEmail());
                System.out.println("  Statut: " + admin.getStatut());
                System.out.println("  Téléphone: " + (admin.getTelephone() != null ? admin.getTelephone() : "(non renseigné)"));
                System.out.println();
            }
            System.out.println("Pour vous connecter, utilisez l'email et le mot de passe");
            System.out.println("que vous avez définis lors de la création du compte.");
            System.out.println();
            System.out.println("Si vous avez oublié le mot de passe:");
            System.out.println("  1. Supprimez l'admin existant avec DeleteAdminDirect");
            System.out.println("  2. Créez un nouvel admin avec CREER_ADMIN.bat");
        }
    }
}






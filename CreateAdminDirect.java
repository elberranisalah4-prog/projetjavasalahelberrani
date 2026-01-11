package com.municipal.dashboard;

public class CreateAdminDirect {
    
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("    CRÉATION D'ADMINISTRATEUR");
        System.out.println("    Dashboard Services Publics");
        System.out.println("=".repeat(60));
        System.out.println();
        
        // Vérifier les arguments
        if (args.length < 4) {
            System.err.println("Usage: java CreateAdminDirect <nom> <prenom> <email> <mot_de_passe> [telephone]");
            System.err.println();
            System.err.println("Exemple:");
            System.err.println("  java CreateAdminDirect Dupont Jean jean@example.com monmotdepasse");
            System.err.println("  java CreateAdminDirect Dupont Jean jean@example.com monmotdepasse 0123456789");
            return;
        }
        
        String nom = args[0];
        String prenom = args[1];
        String email = args[2];
        String password = args[3];
        String telephone = args.length > 4 ? args[4] : null;
        
        // Initialisation de la base de données
        DatabaseManager dbManager = DatabaseManager.getInstance();
        AuthManager authManager = AuthManager.getInstance();
        
        // Vérifier si un admin existe déjà
        if (authManager.hasAdmin()) {
            System.out.println("⚠ ATTENTION: Un administrateur existe déjà dans la base de données!");
            System.out.println("   Seul un administrateur est autorisé.");
            System.out.println();
            System.out.println("Pour créer un nouvel admin, supprimez d'abord l'admin existant avec DeleteAdminDirect");
            return;
        }
        
        // Créer l'admin
        System.out.println("Création de l'administrateur en cours...");
        System.out.println();
        System.out.println("Nom: " + nom);
        System.out.println("Prénom: " + prenom);
        System.out.println("Email: " + email);
        if (telephone != null) {
            System.out.println("Téléphone: " + telephone);
        }
        System.out.println();
        
        boolean created = authManager.createAdmin(nom, prenom, email, password, telephone);
        
        if (created) {
            System.out.println("✓ Administrateur créé avec succès!");
            System.out.println();
            System.out.println("Informations de connexion:");
            System.out.println("  Email: " + email);
            System.out.println("  Mot de passe: " + password);
            System.out.println();
            System.out.println("Vous pouvez maintenant vous connecter au serveur web.");
        } else {
            System.err.println();
            System.err.println("✗ ERREUR: Impossible de créer l'administrateur.");
            System.err.println("   Un administrateur existe peut-être déjà.");
        }
    }
}


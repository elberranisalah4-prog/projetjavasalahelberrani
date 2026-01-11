package com.municipal.dashboard;

import java.util.Scanner;

public class CreateAdmin {
    
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("    CRÉATION D'ADMINISTRATEUR");
        System.out.println("    Dashboard Services Publics");
        System.out.println("=".repeat(60));
        System.out.println();
        
        // Initialisation de la base de données
        DatabaseManager dbManager = DatabaseManager.getInstance();
        AuthManager authManager = AuthManager.getInstance();
        
        // Vérifier si un admin existe déjà
        if (authManager.hasAdmin()) {
            System.out.println("⚠ ATTENTION: Un administrateur existe déjà dans la base de données!");
            System.out.println("   Seul un administrateur est autorisé.");
            System.out.println();
            System.out.print("Voulez-vous continuer quand même? (oui/non): ");
            Scanner scanner = new Scanner(System.in);
            String reponse = scanner.nextLine().trim().toLowerCase();
            if (!reponse.equals("oui") && !reponse.equals("o")) {
                System.out.println("Opération annulée.");
                return;
            }
        }
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Veuillez remplir les informations de l'administrateur:");
        System.out.println();
        
        System.out.print("Nom *: ");
        String nom = scanner.nextLine().trim();
        
        System.out.print("Prénom *: ");
        String prenom = scanner.nextLine().trim();
        
        System.out.print("Email *: ");
        String email = scanner.nextLine().trim();
        
        System.out.print("Mot de passe *: ");
        String password = scanner.nextLine().trim();
        
        System.out.print("Téléphone (optionnel): ");
        String telephone = scanner.nextLine().trim();
        if (telephone.isEmpty()) {
            telephone = null;
        }
        
        // Validation
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.err.println("\n✗ ERREUR: Tous les champs marqués * sont obligatoires!");
            return;
        }
        
        // Créer l'admin
        System.out.println();
        System.out.println("Création de l'administrateur en cours...");
        
        boolean created = authManager.createAdmin(nom, prenom, email, password, telephone);
        
        if (created) {
            System.out.println();
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
        
        scanner.close();
    }
}


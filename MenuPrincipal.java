package com.municipal.dashboard;

import com.municipal.dashboard.dao.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {
    private Scanner scanner;
    private ServiceDAO serviceDAO;
    private ZoneDAO zoneDAO;
    private AdminDAO adminDAO;
    private PersonnelDAO personnelDAO;
    private IndicatorDAO indicatorDAO;
    private PosteDAO posteDAO;
    private ConsommationDAO consommationDAO;
    private ProductionDAO productionDAO;
    private PredictionDAO predictionDAO;
    private DecisionDAO decisionDAO;
    private NoteDAO noteDAO;

    public MenuPrincipal() {
        this.scanner = new Scanner(System.in);
        this.serviceDAO = new ServiceDAO();
        this.zoneDAO = new ZoneDAO();
        this.adminDAO = new AdminDAO();
        this.personnelDAO = new PersonnelDAO();
        this.indicatorDAO = new IndicatorDAO();
        this.posteDAO = new PosteDAO();
        this.consommationDAO = new ConsommationDAO();
        this.productionDAO = new ProductionDAO();
        this.predictionDAO = new PredictionDAO();
        this.decisionDAO = new DecisionDAO();
        this.noteDAO = new NoteDAO();
    }

    public void afficherMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("    DASHBOARD SERVICES PUBLICS - MENU PRINCIPAL");
            System.out.println("=".repeat(60));
            System.out.println("1.  Gestion des Services");
            System.out.println("2.  Gestion des Zones");
            System.out.println("3.  Gestion des Administrateurs");
            System.out.println("4.  Gestion du Personnel");
            System.out.println("5.  Gestion des Postes");
            System.out.println("6.  Gestion des Consommations");
            System.out.println("7.  Gestion des Productions");
            System.out.println("8.  Gestion des Indicateurs");
            System.out.println("9.  Gestion des Prédictions");
            System.out.println("10. Gestion des Décisions");
            System.out.println("11. Gestion des Notes");
            System.out.println("12. Gestion des Matchs Sportifs");
            System.out.println("0.  Quitter");
            System.out.println("=".repeat(60));
            System.out.print("Votre choix: ");

            int choix = lireEntier();
            switch (choix) {
                case 1 -> menuServices();
                case 2 -> menuZones();
                case 3 -> menuAdmins();
                case 4 -> menuPersonnel();
                case 5 -> menuPostes();
                case 6 -> menuConsommations();
                case 7 -> menuProductions();
                case 8 -> menuIndicateurs();
                case 9 -> menuPredictions();
                case 10 -> menuDecisions();
                case 11 -> menuNotes();
                case 12 -> {
                    System.out.println("\n⚠ Fonctionnalité Sports non implémentée pour le moment.");
                    System.out.println("   Cette option sera disponible dans une prochaine version.");
                }
                // case 12 -> menuSports(); // TODO: Implémenter SportsMenu
                case 0 -> {
                    System.out.println("\nAu revoir!");
                    return;
                }
                default -> System.out.println("Choix invalide!");
            }
        }
    }

    private void menuServices() {
        while (true) {
            System.out.println("\n--- GESTION DES SERVICES ---");
            System.out.println("1. Lister tous les services");
            System.out.println("2. Ajouter un service");
            System.out.println("3. Modifier un service");
            System.out.println("4. Supprimer un service");
            System.out.println("5. Rechercher par type");
            System.out.println("0. Retour");
            System.out.print("Choix: ");
            int choix = lireEntier();
            
            switch (choix) {
                case 1 -> {
                    List<Service> services = serviceDAO.findAll();
                    afficherServices(services);
                }
                case 2 -> ajouterService();
                case 3 -> modifierService();
                case 4 -> supprimerService();
                case 5 -> rechercherServiceParType();
                case 0 -> { return; }
            }
        }
    }

    private void menuZones() {
        while (true) {
            System.out.println("\n--- GESTION DES ZONES ---");
            System.out.println("1. Lister toutes les zones");
            System.out.println("2. Ajouter une zone");
            System.out.println("3. Modifier une zone");
            System.out.println("4. Supprimer une zone");
            System.out.println("0. Retour");
            System.out.print("Choix: ");
            int choix = lireEntier();
            
            switch (choix) {
                case 1 -> {
                    List<Zone> zones = zoneDAO.findAll();
                    afficherZones(zones);
                }
                case 2 -> ajouterZone();
                case 3 -> modifierZone();
                case 4 -> supprimerZone();
                case 0 -> { return; }
            }
        }
    }

    private void menuAdmins() {
        while (true) {
            System.out.println("\n--- GESTION DES ADMINISTRATEURS ---");
            System.out.println("1. Lister tous les admins");
            System.out.println("2. Ajouter un admin");
            System.out.println("3. Modifier un admin");
            System.out.println("4. Supprimer un admin");
            System.out.println("0. Retour");
            System.out.print("Choix: ");
            int choix = lireEntier();
            
            switch (choix) {
                case 1 -> {
                    List<Admin> admins = adminDAO.findAll();
                    afficherAdmins(admins);
                }
                case 2 -> ajouterAdmin();
                case 3 -> modifierAdmin();
                case 4 -> supprimerAdmin();
                case 0 -> { return; }
            }
        }
    }

    private void menuPersonnel() {
        while (true) {
            System.out.println("\n--- GESTION DU PERSONNEL ---");
            System.out.println("1. Lister tout le personnel");
            System.out.println("2. Ajouter un membre");
            System.out.println("3. Modifier un membre");
            System.out.println("4. Supprimer un membre");
            System.out.println("5. Rechercher par département");
            System.out.println("0. Retour");
            System.out.print("Choix: ");
            int choix = lireEntier();
            
            switch (choix) {
                case 1 -> {
                    List<Personnel> personnels = personnelDAO.findAll();
                    afficherPersonnel(personnels);
                }
                case 2 -> ajouterPersonnel();
                case 3 -> modifierPersonnel();
                case 4 -> supprimerPersonnel();
                case 5 -> rechercherPersonnelParDepartement();
                case 0 -> { return; }
            }
        }
    }

    private void menuPostes() {
        while (true) {
            System.out.println("\n--- GESTION DES POSTES ---");
            System.out.println("1. Lister tous les postes");
            System.out.println("2. Ajouter un poste");
            System.out.println("3. Modifier un poste");
            System.out.println("4. Supprimer un poste");
            System.out.println("0. Retour");
            System.out.print("Choix: ");
            int choix = lireEntier();
            
            switch (choix) {
                case 1 -> {
                    List<Poste> postes = posteDAO.findAll();
                    afficherPostes(postes);
                }
                case 2 -> ajouterPoste();
                case 3 -> modifierPoste();
                case 4 -> supprimerPoste();
                case 0 -> { return; }
            }
        }
    }

    private void menuConsommations() {
        while (true) {
            System.out.println("\n--- GESTION DES CONSOMMATIONS ---");
            System.out.println("1. Lister toutes les consommations");
            System.out.println("2. Ajouter une consommation");
            System.out.println("3. Modifier une consommation");
            System.out.println("4. Supprimer une consommation");
            System.out.println("0. Retour");
            System.out.print("Choix: ");
            int choix = lireEntier();
            
            switch (choix) {
                case 1 -> {
                    List<Consommation> consommations = consommationDAO.findAll();
                    afficherConsommations(consommations);
                }
                case 2 -> ajouterConsommation();
                case 3 -> modifierConsommation();
                case 4 -> supprimerConsommation();
                case 0 -> { return; }
            }
        }
    }

    private void menuProductions() {
        while (true) {
            System.out.println("\n--- GESTION DES PRODUCTIONS ---");
            System.out.println("1. Lister toutes les productions");
            System.out.println("2. Ajouter une production");
            System.out.println("3. Modifier une production");
            System.out.println("4. Supprimer une production");
            System.out.println("0. Retour");
            System.out.print("Choix: ");
            int choix = lireEntier();
            
            switch (choix) {
                case 1 -> {
                    List<Production> productions = productionDAO.findAll();
                    afficherProductions(productions);
                }
                case 2 -> ajouterProduction();
                case 3 -> modifierProduction();
                case 4 -> supprimerProduction();
                case 0 -> { return; }
            }
        }
    }

    private void menuIndicateurs() {
        while (true) {
            System.out.println("\n--- GESTION DES INDICATEURS ---");
            System.out.println("1. Lister tous les indicateurs");
            System.out.println("2. Ajouter un indicateur");
            System.out.println("3. Modifier un indicateur");
            System.out.println("4. Supprimer un indicateur");
            System.out.println("0. Retour");
            System.out.print("Choix: ");
            int choix = lireEntier();
            
            switch (choix) {
                case 1 -> {
                    List<Indicator> indicators = indicatorDAO.findAll();
                    afficherIndicateurs(indicators);
                }
                case 2 -> ajouterIndicateur();
                case 3 -> modifierIndicateur();
                case 4 -> supprimerIndicateur();
                case 0 -> { return; }
            }
        }
    }

    private void menuPredictions() {
        while (true) {
            System.out.println("\n--- GESTION DES PRÉDICTIONS ---");
            System.out.println("1. Lister toutes les prédictions");
            System.out.println("2. Ajouter une prédiction");
            System.out.println("3. Modifier une prédiction");
            System.out.println("4. Supprimer une prédiction");
            System.out.println("0. Retour");
            System.out.print("Choix: ");
            int choix = lireEntier();
            
            switch (choix) {
                case 1 -> {
                    List<Prediction> predictions = predictionDAO.findAll();
                    afficherPredictions(predictions);
                }
                case 2 -> ajouterPrediction();
                case 3 -> modifierPrediction();
                case 4 -> supprimerPrediction();
                case 0 -> { return; }
            }
        }
    }

    private void menuDecisions() {
        while (true) {
            System.out.println("\n--- GESTION DES DÉCISIONS ---");
            System.out.println("1. Lister toutes les décisions");
            System.out.println("2. Ajouter une décision");
            System.out.println("3. Modifier une décision");
            System.out.println("4. Supprimer une décision");
            System.out.println("0. Retour");
            System.out.print("Choix: ");
            int choix = lireEntier();
            
            switch (choix) {
                case 1 -> {
                    List<Decision> decisions = decisionDAO.findAll();
                    afficherDecisions(decisions);
                }
                case 2 -> ajouterDecision();
                case 3 -> modifierDecision();
                case 4 -> supprimerDecision();
                case 0 -> { return; }
            }
        }
    }

    private void menuNotes() {
        while (true) {
            System.out.println("\n--- GESTION DES NOTES ---");
            System.out.println("1. Lister toutes les notes");
            System.out.println("2. Ajouter une note");
            System.out.println("3. Modifier une note");
            System.out.println("4. Supprimer une note");
            System.out.println("0. Retour");
            System.out.print("Choix: ");
            int choix = lireEntier();
            
            switch (choix) {
                case 1 -> {
                    List<Note> notes = noteDAO.findAll();
                    afficherNotes(notes);
                }
                case 2 -> ajouterNote();
                case 3 -> modifierNote();
                case 4 -> supprimerNote();
                case 0 -> { return; }
            }
        }
    }

    // TODO: Implémenter la classe SportsMenu
    /*
    private void menuSports() {
        SportsMenu sportsMenu = new SportsMenu();
        sportsMenu.afficherMenu();
    }
    */

    // Méthodes d'affichage
    private void afficherServices(List<Service> services) {
        if (services.isEmpty()) {
            System.out.println("Aucun service trouvé.");
            return;
        }
        System.out.println("\nListe des services:");
        System.out.println("-".repeat(80));
        for (Service s : services) {
            System.out.printf("ID: %d | Type: %s | Nom: %s | Description: %s%n",
                    s.getId(), s.getType(), s.getNom(), s.getDescription());
        }
    }

    private void afficherZones(List<Zone> zones) {
        if (zones.isEmpty()) {
            System.out.println("Aucune zone trouvée.");
            return;
        }
        System.out.println("\nListe des zones:");
        System.out.println("-".repeat(80));
        for (Zone z : zones) {
            System.out.printf("ID: %d | Nom: %s | Code: %s | Description: %s%n",
                    z.getId(), z.getNom(), z.getCode(), z.getDescription());
        }
    }

    private void afficherAdmins(List<Admin> admins) {
        if (admins.isEmpty()) {
            System.out.println("Aucun administrateur trouvé.");
            return;
        }
        System.out.println("\nListe des administrateurs:");
        System.out.println("-".repeat(80));
        for (Admin a : admins) {
            System.out.printf("ID: %d | %s %s | Email: %s | Rôle: %s | Statut: %s%n",
                    a.getId(), a.getPrenom(), a.getNom(), a.getEmail(), a.getRole(), a.getStatut());
        }
    }

    private void afficherPersonnel(List<Personnel> personnels) {
        if (personnels.isEmpty()) {
            System.out.println("Aucun personnel trouvé.");
            return;
        }
        System.out.println("\nListe du personnel:");
        System.out.println("-".repeat(80));
        for (Personnel p : personnels) {
            System.out.printf("ID: %d | %s %s | Poste: %s | Département: %s | Statut: %s%n",
                    p.getId(), p.getPrenom(), p.getNom(), p.getPoste(), p.getDepartement(), p.getStatut());
        }
    }

    private void afficherPostes(List<Poste> postes) {
        if (postes.isEmpty()) {
            System.out.println("Aucun poste trouvé.");
            return;
        }
        System.out.println("\nListe des postes:");
        System.out.println("-".repeat(80));
        for (Poste p : postes) {
            System.out.printf("ID: %d | %s | Département: %s | Salaire: %.2f - %.2f%n",
                    p.getId(), p.getTitre(), p.getDepartement(), p.getSalaireMin(), p.getSalaireMax());
        }
    }

    private void afficherConsommations(List<Consommation> consommations) {
        if (consommations.isEmpty()) {
            System.out.println("Aucune consommation trouvée.");
            return;
        }
        System.out.println("\nListe des consommations:");
        System.out.println("-".repeat(80));
        for (Consommation c : consommations) {
            System.out.printf("ID: %d | Valeur: %.2f | Date: %s | Service: %s | Zone: %s%n",
                    c.getId(), c.getValeur(), c.getDate(), c.getServiceType(), c.getZone());
        }
    }

    private void afficherProductions(List<Production> productions) {
        if (productions.isEmpty()) {
            System.out.println("Aucune production trouvée.");
            return;
        }
        System.out.println("\nListe des productions:");
        System.out.println("-".repeat(80));
        for (Production p : productions) {
            System.out.printf("ID: %d | Valeur: %.2f | Date: %s | Service: %s | Zone: %s%n",
                    p.getId(), p.getValeur(), p.getDate(), p.getServiceType(), p.getZone());
        }
    }

    private void afficherIndicateurs(List<Indicator> indicators) {
        if (indicators.isEmpty()) {
            System.out.println("Aucun indicateur trouvé.");
            return;
        }
        System.out.println("\nListe des indicateurs:");
        System.out.println("-".repeat(80));
        for (Indicator i : indicators) {
            System.out.printf("ID: %d | %s | Valeur: %.2f | Cible: %.2f | Statut: %s%n",
                    i.getId(), i.getNom(), i.getValeur(), i.getValeurCible(), i.getStatut());
        }
    }

    private void afficherPredictions(List<Prediction> predictions) {
        if (predictions.isEmpty()) {
            System.out.println("Aucune prédiction trouvée.");
            return;
        }
        System.out.println("\nListe des prédictions:");
        System.out.println("-".repeat(80));
        for (Prediction p : predictions) {
            System.out.printf("ID: %d | Service: %s | Valeur prédite: %.2f | Confiance: %.2f%% | Zone: %s%n",
                    p.getId(), p.getServiceType(), p.getValeurPredite(), p.getConfiance(), p.getZone());
        }
    }

    private void afficherDecisions(List<Decision> decisions) {
        if (decisions.isEmpty()) {
            System.out.println("Aucune décision trouvée.");
            return;
        }
        System.out.println("\nListe des décisions:");
        System.out.println("-".repeat(80));
        for (Decision d : decisions) {
            System.out.printf("ID: %d | %s | Type: %s | Statut: %s | Auteur: %s%n",
                    d.getId(), d.getTitre(), d.getType(), d.getStatut(), d.getAuteur());
        }
    }

    private void afficherNotes(List<Note> notes) {
        if (notes.isEmpty()) {
            System.out.println("Aucune note trouvée.");
            return;
        }
        System.out.println("\nListe des notes:");
        System.out.println("-".repeat(80));
        for (Note n : notes) {
            System.out.printf("ID: %d | Auteur: %s | Type: %s | Date: %s%n",
                    n.getId(), n.getAuteur(), n.getType(), n.getDateCreation());
            System.out.println("  Contenu: " + n.getContenu());
        }
    }

    // Méthodes d'ajout
    private void ajouterService() {
        System.out.print("Type de service: ");
        String type = scanner.nextLine();
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        
        Service service = new Service(type, nom, description);
        serviceDAO.insert(service);
        System.out.println("✓ Service ajouté avec succès!");
    }

    private void ajouterZone() {
        System.out.print("Nom de la zone: ");
        String nom = scanner.nextLine();
        System.out.print("Code: ");
        String code = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        
        Zone zone = new Zone(nom, code, description);
        zoneDAO.insert(zone);
        System.out.println("✓ Zone ajoutée avec succès!");
    }

    private void ajouterAdmin() {
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        System.out.print("Prénom: ");
        String prenom = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String motDePasse = scanner.nextLine();
        System.out.print("Rôle: ");
        String role = scanner.nextLine();
        System.out.print("Téléphone: ");
        String telephone = scanner.nextLine();
        
        Admin admin = new Admin(nom, prenom, email, motDePasse, role, telephone);
        try {
            adminDAO.insert(admin);
            System.out.println("✓ Administrateur ajouté avec succès!");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout de l'administrateur: " + e.getMessage());
        }
    }

    private void ajouterPersonnel() {
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        System.out.print("Prénom: ");
        String prenom = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Téléphone: ");
        String telephone = scanner.nextLine();
        System.out.print("Poste: ");
        String poste = scanner.nextLine();
        System.out.print("Département: ");
        String departement = scanner.nextLine();
        System.out.print("Matricule: ");
        String matricule = scanner.nextLine();
        System.out.print("Statut (ACTIF/INACTIF): ");
        String statut = scanner.nextLine();
        
        Personnel personnel = new Personnel(nom, prenom, email, telephone, poste, departement, LocalDateTime.now(), statut);
        personnel.setMatricule(matricule);
        personnelDAO.insert(personnel);
        System.out.println("✓ Personnel ajouté avec succès!");
    }

    private void ajouterPoste() {
        System.out.print("Titre: ");
        String titre = scanner.nextLine();
        System.out.print("Type: ");
        String type = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Département: ");
        String departement = scanner.nextLine();
        System.out.print("Niveau: ");
        String niveau = scanner.nextLine();
        System.out.print("Salaire minimum: ");
        double salaireMin = lireDouble();
        System.out.print("Salaire maximum: ");
        double salaireMax = lireDouble();
        
        Poste poste = new Poste(titre, type, description, departement, niveau, salaireMin, salaireMax);
        posteDAO.insert(poste);
        System.out.println("✓ Poste ajouté avec succès!");
    }

    private void ajouterConsommation() {
        System.out.print("Valeur: ");
        double valeur = lireDouble();
        System.out.print("Type de service: ");
        String serviceType = scanner.nextLine();
        System.out.print("Zone: ");
        String zone = scanner.nextLine();
        
        Consommation consommation = new Consommation(valeur, LocalDateTime.now(), serviceType, zone);
        consommationDAO.insert(consommation);
        System.out.println("✓ Consommation ajoutée avec succès!");
    }

    private void ajouterProduction() {
        System.out.print("Valeur: ");
        double valeur = lireDouble();
        System.out.print("Type de service: ");
        String serviceType = scanner.nextLine();
        System.out.print("Zone: ");
        String zone = scanner.nextLine();
        
        Production production = new Production(valeur, LocalDateTime.now(), serviceType, zone);
        productionDAO.insert(production);
        System.out.println("✓ Production ajoutée avec succès!");
    }

    private void ajouterIndicateur() {
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        System.out.print("Type: ");
        String type = scanner.nextLine();
        System.out.print("Valeur: ");
        double valeur = lireDouble();
        System.out.print("Valeur cible: ");
        double valeurCible = lireDouble();
        System.out.print("Unité: ");
        String unite = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Statut: ");
        String statut = scanner.nextLine();
        
        Indicator indicator = new Indicator(nom, type, valeur, LocalDateTime.now(), unite);
        indicator.setValeurCible(valeurCible);
        indicator.setDescription(description);
        indicator.setStatut(statut);
        indicatorDAO.insert(indicator);
        System.out.println("✓ Indicateur ajouté avec succès!");
    }

    private void ajouterPrediction() {
        System.out.print("Type de service: ");
        String serviceType = scanner.nextLine();
        System.out.print("Valeur prédite: ");
        double valeurPredite = lireDouble();
        System.out.print("Confiance (%): ");
        double confiance = lireDouble();
        System.out.print("Zone: ");
        String zone = scanner.nextLine();
        System.out.print("Type de prédiction: ");
        String typePrediction = scanner.nextLine();
        System.out.print("Recommandation: ");
        String recommandation = scanner.nextLine();
        
        Prediction prediction = new Prediction(serviceType, LocalDateTime.now().plusMonths(1), valeurPredite, confiance, zone, typePrediction);
        prediction.setRecommandation(recommandation);
        predictionDAO.insert(prediction);
        System.out.println("✓ Prédiction ajoutée avec succès!");
    }

    private void ajouterDecision() {
        System.out.print("Titre: ");
        String titre = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Type: ");
        String type = scanner.nextLine();
        System.out.print("Statut: ");
        String statut = scanner.nextLine();
        System.out.print("Auteur: ");
        String auteur = scanner.nextLine();
        System.out.print("Service concerné: ");
        String serviceConcerne = scanner.nextLine();
        System.out.print("Zone concernée: ");
        String zoneConcernee = scanner.nextLine();
        System.out.print("Justification: ");
        String justification = scanner.nextLine();
        
        Decision decision = new Decision(titre, description, type, statut, LocalDateTime.now(), auteur, serviceConcerne);
        decision.setZoneConcernee(zoneConcernee);
        decision.setJustification(justification);
        decisionDAO.insert(decision);
        System.out.println("✓ Décision ajoutée avec succès!");
    }

    private void ajouterNote() {
        System.out.print("Contenu: ");
        String contenu = scanner.nextLine();
        System.out.print("Auteur: ");
        String auteur = scanner.nextLine();
        System.out.print("Type: ");
        String type = scanner.nextLine();
        
        Note note = new Note(contenu, LocalDateTime.now(), auteur, type);
        noteDAO.insert(note);
        System.out.println("✓ Note ajoutée avec succès!");
    }

    // Méthodes de modification et suppression (simplifiées)
    private void modifierService() {
        System.out.print("ID du service à modifier: ");
        Long id = lireLong();
        Service service = serviceDAO.findById(id);
        if (service == null) {
            System.out.println("Service non trouvé!");
            return;
        }
        System.out.print("Nouveau type: ");
        service.setType(scanner.nextLine());
        System.out.print("Nouveau nom: ");
        service.setNom(scanner.nextLine());
        System.out.print("Nouvelle description: ");
        service.setDescription(scanner.nextLine());
        serviceDAO.update(service);
        System.out.println("✓ Service modifié avec succès!");
    }

    private void modifierZone() {
        System.out.print("ID de la zone à modifier: ");
        Long id = lireLong();
        Zone zone = zoneDAO.findById(id);
        if (zone == null) {
            System.out.println("Zone non trouvée!");
            return;
        }
        System.out.print("Nouveau nom: ");
        zone.setNom(scanner.nextLine());
        System.out.print("Nouveau code: ");
        zone.setCode(scanner.nextLine());
        System.out.print("Nouvelle description: ");
        zone.setDescription(scanner.nextLine());
        zoneDAO.update(zone);
        System.out.println("✓ Zone modifiée avec succès!");
    }

    private void modifierAdmin() {
        System.out.print("ID de l'admin à modifier: ");
        Long id = lireLong();
        Admin admin = adminDAO.findById(id);
        if (admin == null) {
            System.out.println("Admin non trouvé!");
            return;
        }
        System.out.print("Nouveau nom: ");
        admin.setNom(scanner.nextLine());
        System.out.print("Nouveau prénom: ");
        admin.setPrenom(scanner.nextLine());
        System.out.print("Nouveau rôle: ");
        admin.setRole(scanner.nextLine());
        adminDAO.update(admin);
        System.out.println("✓ Admin modifié avec succès!");
    }

    private void modifierPersonnel() {
        System.out.print("ID du personnel à modifier: ");
        Long id = lireLong();
        Personnel personnel = personnelDAO.findById(id);
        if (personnel == null) {
            System.out.println("Personnel non trouvé!");
            return;
        }
        System.out.print("Nouveau poste: ");
        personnel.setPoste(scanner.nextLine());
        System.out.print("Nouveau département: ");
        personnel.setDepartement(scanner.nextLine());
        System.out.print("Nouveau statut: ");
        personnel.setStatut(scanner.nextLine());
        personnelDAO.update(personnel);
        System.out.println("✓ Personnel modifié avec succès!");
    }

    private void modifierPoste() {
        System.out.print("ID du poste à modifier: ");
        Long id = lireLong();
        Poste poste = posteDAO.findById(id);
        if (poste == null) {
            System.out.println("Poste non trouvé!");
            return;
        }
        System.out.print("Nouveau titre: ");
        poste.setTitre(scanner.nextLine());
        System.out.print("Nouveau salaire min: ");
        poste.setSalaireMin(lireDouble());
        System.out.print("Nouveau salaire max: ");
        poste.setSalaireMax(lireDouble());
        posteDAO.update(poste);
        System.out.println("✓ Poste modifié avec succès!");
    }

    private void modifierConsommation() {
        System.out.print("ID de la consommation à modifier: ");
        Long id = lireLong();
        Consommation consommation = consommationDAO.findById(id);
        if (consommation == null) {
            System.out.println("Consommation non trouvée!");
            return;
        }
        System.out.print("Nouvelle valeur: ");
        consommation.setValeur(lireDouble());
        consommationDAO.update(consommation);
        System.out.println("✓ Consommation modifiée avec succès!");
    }

    private void modifierProduction() {
        System.out.print("ID de la production à modifier: ");
        Long id = lireLong();
        Production production = productionDAO.findById(id);
        if (production == null) {
            System.out.println("Production non trouvée!");
            return;
        }
        System.out.print("Nouvelle valeur: ");
        production.setValeur(lireDouble());
        productionDAO.update(production);
        System.out.println("✓ Production modifiée avec succès!");
    }

    private void modifierIndicateur() {
        System.out.print("ID de l'indicateur à modifier: ");
        Long id = lireLong();
        Indicator indicator = indicatorDAO.findById(id);
        if (indicator == null) {
            System.out.println("Indicateur non trouvé!");
            return;
        }
        System.out.print("Nouvelle valeur: ");
        indicator.setValeur(lireDouble());
        System.out.print("Nouvelle valeur cible: ");
        indicator.setValeurCible(lireDouble());
        indicatorDAO.update(indicator);
        System.out.println("✓ Indicateur modifié avec succès!");
    }

    private void modifierPrediction() {
        System.out.print("ID de la prédiction à modifier: ");
        Long id = lireLong();
        Prediction prediction = predictionDAO.findById(id);
        if (prediction == null) {
            System.out.println("Prédiction non trouvée!");
            return;
        }
        System.out.print("Nouvelle valeur prédite: ");
        prediction.setValeurPredite(lireDouble());
        System.out.print("Nouvelle confiance: ");
        prediction.setConfiance(lireDouble());
        predictionDAO.update(prediction);
        System.out.println("✓ Prédiction modifiée avec succès!");
    }

    private void modifierDecision() {
        System.out.print("ID de la décision à modifier: ");
        Long id = lireLong();
        Decision decision = decisionDAO.findById(id);
        if (decision == null) {
            System.out.println("Décision non trouvée!");
            return;
        }
        System.out.print("Nouveau statut: ");
        decision.setStatut(scanner.nextLine());
        decisionDAO.update(decision);
        System.out.println("✓ Décision modifiée avec succès!");
    }

    private void modifierNote() {
        System.out.print("ID de la note à modifier: ");
        Long id = lireLong();
        Note note = noteDAO.findById(id);
        if (note == null) {
            System.out.println("Note non trouvée!");
            return;
        }
        System.out.print("Nouveau contenu: ");
        note.setContenu(scanner.nextLine());
        noteDAO.update(note);
        System.out.println("✓ Note modifiée avec succès!");
    }

    // Méthodes de suppression
    private void supprimerService() {
        System.out.print("ID du service à supprimer: ");
        Long id = lireLong();
        serviceDAO.delete(id);
        System.out.println("✓ Service supprimé!");
    }

    private void supprimerZone() {
        System.out.print("ID de la zone à supprimer: ");
        Long id = lireLong();
        zoneDAO.delete(id);
        System.out.println("✓ Zone supprimée!");
    }

    private void supprimerAdmin() {
        System.out.print("ID de l'admin à supprimer: ");
        Long id = lireLong();
        adminDAO.delete(id);
        System.out.println("✓ Admin supprimé!");
    }

    private void supprimerPersonnel() {
        System.out.print("ID du personnel à supprimer: ");
        Long id = lireLong();
        personnelDAO.delete(id);
        System.out.println("✓ Personnel supprimé!");
    }

    private void supprimerPoste() {
        System.out.print("ID du poste à supprimer: ");
        Long id = lireLong();
        posteDAO.delete(id);
        System.out.println("✓ Poste supprimé!");
    }

    private void supprimerConsommation() {
        System.out.print("ID de la consommation à supprimer: ");
        Long id = lireLong();
        consommationDAO.delete(id);
        System.out.println("✓ Consommation supprimée!");
    }

    private void supprimerProduction() {
        System.out.print("ID de la production à supprimer: ");
        Long id = lireLong();
        productionDAO.delete(id);
        System.out.println("✓ Production supprimée!");
    }

    private void supprimerIndicateur() {
        System.out.print("ID de l'indicateur à supprimer: ");
        Long id = lireLong();
        indicatorDAO.delete(id);
        System.out.println("✓ Indicateur supprimé!");
    }

    private void supprimerPrediction() {
        System.out.print("ID de la prédiction à supprimer: ");
        Long id = lireLong();
        predictionDAO.delete(id);
        System.out.println("✓ Prédiction supprimée!");
    }

    private void supprimerDecision() {
        System.out.print("ID de la décision à supprimer: ");
        Long id = lireLong();
        decisionDAO.delete(id);
        System.out.println("✓ Décision supprimée!");
    }

    private void supprimerNote() {
        System.out.print("ID de la note à supprimer: ");
        Long id = lireLong();
        noteDAO.delete(id);
        System.out.println("✓ Note supprimée!");
    }

    // Méthodes de recherche
    private void rechercherServiceParType() {
        System.out.print("Type de service: ");
        String type = scanner.nextLine();
        List<Service> services = serviceDAO.findByType(type);
        afficherServices(services);
    }

    private void rechercherPersonnelParDepartement() {
        System.out.print("Département: ");
        String departement = scanner.nextLine();
        List<Personnel> personnels = personnelDAO.findByDepartement(departement);
        afficherPersonnel(personnels);
    }

    // Utilitaires
    private int lireEntier() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private long lireLong() {
        try {
            return Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private double lireDouble() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}


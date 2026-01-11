package com.municipal.dashboard;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class RapportFormulaires {
    
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("    RAPPORT COMPLET DES FORMULAIRES");
        System.out.println("    Génération d'un rapport détaillé de tous les formulaires");
        System.out.println("=".repeat(70));
        System.out.println();
        
        StringBuilder rapport = new StringBuilder();
        rapport.append("=".repeat(70)).append("\n");
        rapport.append("    RAPPORT COMPLET DES FORMULAIRES CRUD\n");
        rapport.append("    Date: ").append(new java.util.Date()).append("\n");
        rapport.append("=".repeat(70)).append("\n\n");
        
        // Définir tous les modules avec leurs configurations
        List<ModuleConfig> modules = new ArrayList<>();
        
        // Zones
        modules.add(new ModuleConfig(
            "Zones", "🗺️", "/api/zones", "/zones",
            new String[]{"nom", "code", "description"},
            new String[]{"Nom", "Code", "Description"},
            new String[]{"text", "text", "textarea"},
            Zone.class
        ));
        
        // Services
        modules.add(new ModuleConfig(
            "Services", "📄", "/api/services", "/services",
            new String[]{"type", "nom", "description"},
            new String[]{"Type", "Nom", "Description"},
            new String[]{"text", "text", "textarea"},
            Service.class
        ));
        
        // Personnel
        modules.add(new ModuleConfig(
            "Personnel", "👥", "/api/personnel", "/personnel",
            new String[]{"nom", "prenom", "email", "telephone", "poste", "departement", "dateEmbauche", "statut"},
            new String[]{"Nom", "Prénom", "Email", "Téléphone", "Poste", "Département", "Date d'embauche", "Statut"},
            new String[]{"text", "text", "email", "text", "select", "select", "date", "select"},
            Personnel.class
        ));
        
        // Postes
        modules.add(new ModuleConfig(
            "Postes", "💼", "/api/postes", "/postes",
            new String[]{"titre", "type", "description", "departement", "niveau", "salaireMin", "salaireMax"},
            new String[]{"Titre", "Type", "Description", "Département", "Niveau", "Salaire Min", "Salaire Max"},
            new String[]{"text", "text", "textarea", "text", "text", "number", "number"},
            Poste.class
        ));
        
        // Consommations
        modules.add(new ModuleConfig(
            "Consommations", "📊", "/api/consommations", "/consommations",
            new String[]{"valeur", "date", "serviceType", "zone"},
            new String[]{"Valeur", "Date", "Type de Service", "Zone"},
            new String[]{"number", "datetime-local", "select-services", "select-zones"},
            Consommation.class
        ));
        
        // Productions
        modules.add(new ModuleConfig(
            "Productions", "⚡", "/api/productions", "/productions",
            new String[]{"valeur", "date", "serviceType", "zone"},
            new String[]{"Valeur", "Date", "Type de Service", "Zone"},
            new String[]{"number", "datetime-local", "select-services", "select-zones"},
            Production.class
        ));
        
        // Indicateurs
        modules.add(new ModuleConfig(
            "Indicateurs", "✅", "/api/indicateurs", "/indicateurs",
            new String[]{"nom", "type", "valeur", "valeurCible", "unite", "description", "statut", "dateCalcul"},
            new String[]{"Nom", "Type", "Valeur", "Valeur Cible", "Unité", "Description", "Statut", "Date Calcul"},
            new String[]{"text", "text", "number", "number", "text", "textarea", "select", "datetime-local"},
            Indicator.class
        ));
        
        // Prédictions
        modules.add(new ModuleConfig(
            "Prédictions", "🔮", "/api/predictions", "/predictions",
            new String[]{"serviceType", "datePrediction", "valeurPredite", "zone", "confiance", "typePrediction"},
            new String[]{"Type de Service", "Date Prédiction", "Valeur Prédite", "Zone", "Confiance", "Type Prédiction"},
            new String[]{"select-services", "datetime-local", "number", "select-zones", "number", "text"},
            Prediction.class
        ));
        
        // Décisions
        modules.add(new ModuleConfig(
            "Décisions", "⚖️", "/api/decisions", "/decisions",
            new String[]{"titre", "description", "type", "statut", "dateDecision", "dateApplication", "auteur", "serviceConcerne", "zoneConcernee"},
            new String[]{"Titre", "Description", "Type", "Statut", "Date Décision", "Date Application", "Auteur", "Service Concerné", "Zone Concernée"},
            new String[]{"text", "textarea", "text", "select", "datetime-local", "datetime-local", "text", "select-services", "select-zones"},
            Decision.class
        ));
        
        // Notes
        modules.add(new ModuleConfig(
            "Notes", "📝", "/api/notes", "/notes",
            new String[]{"contenu", "dateCreation", "auteur", "type"},
            new String[]{"Contenu", "Date Création", "Auteur", "Type"},
            new String[]{"textarea", "datetime-local", "text", "text"},
            Note.class
        ));
        
        // Générer le rapport pour chaque module
        for (ModuleConfig module : modules) {
            rapport.append("\n").append("=".repeat(70)).append("\n");
            rapport.append("  MODULE: ").append(module.name).append(" ").append(module.icon).append("\n");
            rapport.append("=".repeat(70)).append("\n");
            rapport.append("  Route API: ").append(module.apiPath).append("\n");
            rapport.append("  Route Page: ").append(module.pagePath).append("\n");
            rapport.append("  Classe: ").append(module.clazz.getSimpleName()).append("\n");
            rapport.append("\n");
            rapport.append("  CHAMPS DU FORMULAIRE:\n");
            rapport.append("  ").append("-".repeat(68)).append("\n");
            rapport.append(String.format("  %-20s %-25s %-20s\n", "Nom Champ", "Label", "Type HTML"));
            rapport.append("  ").append("-".repeat(68)).append("\n");
            
            for (int i = 0; i < module.fields.length; i++) {
                String field = module.fields[i];
                String label = module.fieldLabels[i];
                String type = module.fieldTypes[i];
                
                // Vérifier si le getter existe
                String getterName = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
                boolean getterExists = false;
                try {
                    module.clazz.getMethod(getterName);
                    getterExists = true;
                } catch (NoSuchMethodException e) {
                    getterExists = false;
                }
                
                String status = getterExists ? "✓" : "✗";
                rapport.append(String.format("  %s %-18s %-25s %-20s\n", 
                    status, field, label, type));
            }
            
            rapport.append("\n");
            rapport.append("  VÉRIFICATION:\n");
            
            int erreurs = 0;
            for (int i = 0; i < module.fields.length; i++) {
                String field = module.fields[i];
                String getterName = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
                try {
                    module.clazz.getMethod(getterName);
                } catch (NoSuchMethodException e) {
                    erreurs++;
                    rapport.append("    ✗ Getter manquant: ").append(getterName).append("() pour le champ '").append(field).append("'\n");
                }
            }
            
            if (erreurs == 0) {
                rapport.append("    ✅ Tous les getters existent\n");
            } else {
                rapport.append("    ⚠️  ").append(erreurs).append(" getter(s) manquant(s)\n");
            }
            
            rapport.append("\n");
        }
        
        // Résumé
        rapport.append("\n").append("=".repeat(70)).append("\n");
        rapport.append("  RÉSUMÉ\n");
        rapport.append("=".repeat(70)).append("\n");
        rapport.append("  Nombre total de modules: ").append(modules.size()).append("\n");
        
        int totalChamps = 0;
        int totalErreurs = 0;
        for (ModuleConfig module : modules) {
            totalChamps += module.fields.length;
            for (String field : module.fields) {
                String getterName = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
                try {
                    module.clazz.getMethod(getterName);
                } catch (NoSuchMethodException e) {
                    totalErreurs++;
                }
            }
        }
        
        rapport.append("  Nombre total de champs: ").append(totalChamps).append("\n");
        rapport.append("  Erreurs totales: ").append(totalErreurs).append("\n");
        rapport.append("\n");
        
        if (totalErreurs == 0) {
            rapport.append("  ✅ Tous les formulaires sont correctement configurés!\n");
        } else {
            rapport.append("  ⚠️  Des corrections sont nécessaires.\n");
        }
        
        rapport.append("\n").append("=".repeat(70)).append("\n");
        
        // Afficher le rapport
        System.out.println(rapport.toString());
        
        // Sauvegarder dans un fichier
        try {
            String fileName = "RAPPORT_FORMULAIRES_" + 
                new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date()) + ".txt";
            FileWriter writer = new FileWriter(fileName);
            writer.write(rapport.toString());
            writer.close();
            System.out.println("\n✅ Rapport sauvegardé dans: " + fileName);
        } catch (IOException e) {
            System.err.println("\n✗ Erreur lors de la sauvegarde: " + e.getMessage());
        }
    }
    
    static class ModuleConfig {
        String name;
        String icon;
        String apiPath;
        String pagePath;
        String[] fields;
        String[] fieldLabels;
        String[] fieldTypes;
        Class<?> clazz;
        
        ModuleConfig(String name, String icon, String apiPath, String pagePath,
                    String[] fields, String[] fieldLabels, String[] fieldTypes, Class<?> clazz) {
            this.name = name;
            this.icon = icon;
            this.apiPath = apiPath;
            this.pagePath = pagePath;
            this.fields = fields;
            this.fieldLabels = fieldLabels;
            this.fieldTypes = fieldTypes;
            this.clazz = clazz;
        }
    }
}


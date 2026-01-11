package com.municipal.dashboard;

import com.municipal.dashboard.dao.*;
import java.lang.reflect.Method;
import java.util.*;

public class VerifierFormulaires {
    
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("    VÉRIFICATION ET RÉORGANISATION DES FORMULAIRES");
        System.out.println("    Vérification de la correspondance entre formulaires et base de données");
        System.out.println("=".repeat(70));
        System.out.println();
        
        DatabaseManager dbManager = DatabaseManager.getInstance();
        
        // Définir tous les modules avec leurs champs
        Map<String, ModuleInfo> modules = new LinkedHashMap<>();
        
        // Zones
        modules.put("Zones", new ModuleInfo(
            Zone.class,
            new String[]{"nom", "code", "description"},
            new String[]{"Nom", "Code", "Description"},
            "/api/zones"
        ));
        
        // Services
        modules.put("Services", new ModuleInfo(
            Service.class,
            new String[]{"type", "nom", "description"},
            new String[]{"Type", "Nom", "Description"},
            "/api/services"
        ));
        
        // Personnel
        modules.put("Personnel", new ModuleInfo(
            Personnel.class,
            new String[]{"nom", "prenom", "email", "telephone", "poste", "departement", "dateEmbauche", "statut"},
            new String[]{"Nom", "Prénom", "Email", "Téléphone", "Poste", "Département", "Date d'embauche", "Statut"},
            "/api/personnel"
        ));
        
        // Postes
        modules.put("Postes", new ModuleInfo(
            Poste.class,
            new String[]{"titre", "type", "description", "departement", "niveau", "salaireMin", "salaireMax"},
            new String[]{"Titre", "Type", "Description", "Département", "Niveau", "Salaire Min", "Salaire Max"},
            "/api/postes"
        ));
        
        // Consommations
        modules.put("Consommations", new ModuleInfo(
            Consommation.class,
            new String[]{"valeur", "date", "serviceType", "zone"},
            new String[]{"Valeur", "Date", "Type de Service", "Zone"},
            "/api/consommations"
        ));
        
        // Productions
        modules.put("Productions", new ModuleInfo(
            Production.class,
            new String[]{"valeur", "date", "serviceType", "zone"},
            new String[]{"Valeur", "Date", "Type de Service", "Zone"},
            "/api/productions"
        ));
        
        // Indicateurs
        modules.put("Indicateurs", new ModuleInfo(
            Indicator.class,
            new String[]{"nom", "type", "valeur", "valeurCible", "unite", "description", "statut", "dateCalcul"},
            new String[]{"Nom", "Type", "Valeur", "Valeur Cible", "Unité", "Description", "Statut", "Date Calcul"},
            "/api/indicateurs"
        ));
        
        // Prédictions
        modules.put("Prédictions", new ModuleInfo(
            Prediction.class,
            new String[]{"serviceType", "datePrediction", "valeurPredite", "zone", "confiance", "typePrediction"},
            new String[]{"Type de Service", "Date Prédiction", "Valeur Prédite", "Zone", "Confiance", "Type Prédiction"},
            "/api/predictions"
        ));
        
        // Décisions
        modules.put("Décisions", new ModuleInfo(
            Decision.class,
            new String[]{"titre", "description", "type", "statut", "dateDecision", "dateApplication", "auteur", "serviceConcerne", "zoneConcernee"},
            new String[]{"Titre", "Description", "Type", "Statut", "Date Décision", "Date Application", "Auteur", "Service Concerné", "Zone Concernée"},
            "/api/decisions"
        ));
        
        // Notes
        modules.put("Notes", new ModuleInfo(
            Note.class,
            new String[]{"contenu", "dateCreation", "auteur", "type"},
            new String[]{"Contenu", "Date Création", "Auteur", "Type"},
            "/api/notes"
        ));
        
        // Vérifier chaque module
        System.out.println("🔍 VÉRIFICATION DES MODULES:");
        System.out.println();
        
        int totalErreurs = 0;
        int totalAvertissements = 0;
        
        for (Map.Entry<String, ModuleInfo> entry : modules.entrySet()) {
            String moduleName = entry.getKey();
            ModuleInfo info = entry.getValue();
            
            System.out.println("📋 " + moduleName + ":");
            System.out.println("   Classe: " + info.clazz.getSimpleName());
            System.out.println("   API: " + info.apiPath);
            System.out.println("   Champs définis: " + Arrays.toString(info.fields));
            System.out.println();
            
            // Vérifier que tous les getters existent
            int erreurs = 0;
            int avertissements = 0;
            
            for (String field : info.fields) {
                String getterName = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
                try {
                    Method getter = info.clazz.getMethod(getterName);
                    System.out.println("   ✓ " + field + " → " + getterName + "() existe");
                } catch (NoSuchMethodException e) {
                    System.out.println("   ✗ " + field + " → " + getterName + "() MANQUANT!");
                    erreurs++;
                } catch (Exception e) {
                    System.out.println("   ⚠ " + field + " → Erreur: " + e.getMessage());
                    avertissements++;
                }
            }
            
            totalErreurs += erreurs;
            totalAvertissements += avertissements;
            
            if (erreurs == 0 && avertissements == 0) {
                System.out.println("   ✅ Module OK");
            } else {
                System.out.println("   ⚠️  " + erreurs + " erreur(s), " + avertissements + " avertissement(s)");
            }
            System.out.println();
        }
        
        // Résumé
        System.out.println("=".repeat(70));
        System.out.println("📊 RÉSUMÉ:");
        System.out.println("   Modules vérifiés: " + modules.size());
        System.out.println("   Erreurs totales: " + totalErreurs);
        System.out.println("   Avertissements: " + totalAvertissements);
        System.out.println();
        
        if (totalErreurs == 0) {
            System.out.println("✅ Tous les modules sont correctement configurés!");
        } else {
            System.out.println("❌ Des corrections sont nécessaires. Vérifiez les erreurs ci-dessus.");
        }
        
        // Fermeture
        if (dbManager != null) {
            dbManager.close();
        }
    }
    
    static class ModuleInfo {
        Class<?> clazz;
        String[] fields;
        String[] fieldLabels;
        String apiPath;
        
        ModuleInfo(Class<?> clazz, String[] fields, String[] fieldLabels, String apiPath) {
            this.clazz = clazz;
            this.fields = fields;
            this.fieldLabels = fieldLabels;
            this.apiPath = apiPath;
        }
    }
}


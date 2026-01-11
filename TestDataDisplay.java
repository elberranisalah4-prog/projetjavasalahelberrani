package com.municipal.dashboard;

import com.municipal.dashboard.dao.ConsommationDAO;
import com.municipal.dashboard.dao.ProductionDAO;
import java.util.List;

public class TestDataDisplay {
    
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("    TEST D'AFFICHAGE DES DONNÉES");
        System.out.println("=".repeat(60));
        System.out.println();
        
        DatabaseManager dbManager = DatabaseManager.getInstance();
        
        // Tester les Consommations
        System.out.println("📊 CONSOMMATIONS:");
        System.out.println();
        ConsommationDAO consommationDAO = new ConsommationDAO();
        List<Consommation> consommations = consommationDAO.findAll();
        
        if (consommations.isEmpty()) {
            System.out.println("  Aucune consommation trouvée.");
        } else {
            System.out.println("  Nombre de consommations: " + consommations.size());
            System.out.println();
            for (Consommation c : consommations) {
                System.out.println("  ID: " + c.getId());
                System.out.println("    Valeur: " + (c.getValeur() != null ? c.getValeur() : "NULL"));
                System.out.println("    Date: " + (c.getDate() != null ? c.getDate() : "NULL"));
                System.out.println("    ServiceType: '" + (c.getServiceType() != null ? c.getServiceType() : "NULL") + "'");
                System.out.println("    Zone: '" + (c.getZone() != null ? c.getZone() : "NULL") + "'");
                System.out.println();
            }
        }
        
        // Tester les Productions
        System.out.println("⚡ PRODUCTIONS:");
        System.out.println();
        ProductionDAO productionDAO = new ProductionDAO();
        List<Production> productions = productionDAO.findAll();
        
        if (productions.isEmpty()) {
            System.out.println("  Aucune production trouvée.");
        } else {
            System.out.println("  Nombre de productions: " + productions.size());
            System.out.println();
            for (Production p : productions) {
                System.out.println("  ID: " + p.getId());
                System.out.println("    Valeur: " + (p.getValeur() != null ? p.getValeur() : "NULL"));
                System.out.println("    Date: " + (p.getDate() != null ? p.getDate() : "NULL"));
                System.out.println("    ServiceType: '" + (p.getServiceType() != null ? p.getServiceType() : "NULL") + "'");
                System.out.println("    Zone: '" + (p.getZone() != null ? p.getZone() : "NULL") + "'");
                System.out.println();
            }
        }
        
        System.out.println("💡 Si les données affichent NULL, cela signifie que les valeurs dans");
        System.out.println("   la base de données sont NULL. Les corrections apportées aux DAOs");
        System.out.println("   devraient maintenant convertir ces NULL en valeurs par défaut.");
        
        if (dbManager != null) {
            dbManager.close();
        }
    }
}


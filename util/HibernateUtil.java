package com.municipal.dashboard.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

/**
 * Classe utilitaire pour gérer la SessionFactory Hibernate
 * Utilise le pattern Singleton pour garantir une seule instance
 */
public class HibernateUtil {
    private static SessionFactory sessionFactory;
    
    static {
        try {
            // Charger la configuration depuis hibernate.cfg.xml
            Configuration configuration = new Configuration().configure();
            
            // Si le fichier est dans resources, utiliser configure("hibernate.cfg.xml")
            // Sinon, Hibernate cherchera automatiquement dans le classpath
            try {
                configuration.configure("hibernate.cfg.xml");
            } catch (Exception e) {
                // Si le fichier n'est pas trouvé, utiliser la configuration par défaut
                System.err.println("⚠ Fichier hibernate.cfg.xml non trouvé, utilisation de la configuration par défaut");
                configureDefault(configuration);
            }
            
            // Construire la SessionFactory
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();
            
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            System.out.println("✓ Hibernate SessionFactory initialisée avec succès");
            
        } catch (Exception e) {
            System.err.println("✗ ERREUR lors de l'initialisation de Hibernate: " + e.getMessage());
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }
    
    /**
     * Configuration par défaut si le fichier hibernate.cfg.xml n'est pas trouvé
     */
    private static void configureDefault(Configuration configuration) {
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", 
            "jdbc:mysql://localhost:3306/services_publics?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true");
        configuration.setProperty("hibernate.connection.username", "root");
        configuration.setProperty("hibernate.connection.password", "");
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.format_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
    }
    
    /**
     * Obtenir l'instance de SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            throw new IllegalStateException("SessionFactory n'est pas initialisée!");
        }
        return sessionFactory;
    }
    
    /**
     * Fermer la SessionFactory
     * À appeler à la fermeture de l'application
     */
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
            System.out.println("✓ Hibernate SessionFactory fermée");
        }
    }
}


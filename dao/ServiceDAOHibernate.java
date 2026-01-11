package com.municipal.dashboard.dao;

import com.municipal.dashboard.Service;
import com.municipal.dashboard.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

/**
 * DAO pour l'entité Service utilisant Hibernate
 * Version Hibernate du ServiceDAO original
 */
public class ServiceDAOHibernate {
    private SessionFactory sessionFactory;
    
    public ServiceDAOHibernate() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }
    
    /**
     * Insérer un nouveau service
     */
    public void insert(Service service) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(service);
            transaction.commit();
            System.out.println("✓ Service inséré avec Hibernate: " + service.getNom());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("✗ Erreur lors de l'insertion du service: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Trouver tous les services
     */
    public List<Service> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Service> query = session.createQuery("FROM Service ORDER BY nom", Service.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("✗ Erreur lors de la récupération des services: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }
    
    /**
     * Trouver un service par ID
     */
    public Service findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Service.class, id);
        } catch (Exception e) {
            System.err.println("✗ Erreur lors de la recherche du service: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Trouver des services par type
     */
    public List<Service> findByType(String type) {
        try (Session session = sessionFactory.openSession()) {
            Query<Service> query = session.createQuery(
                "FROM Service WHERE type = :type ORDER BY nom", Service.class);
            query.setParameter("type", type);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("✗ Erreur lors de la recherche par type: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }
    
    /**
     * Mettre à jour un service
     */
    public void update(Service service) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(service);
            transaction.commit();
            System.out.println("✓ Service mis à jour avec Hibernate: " + service.getNom());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("✗ Erreur lors de la mise à jour du service: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Supprimer un service
     */
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Service service = session.get(Service.class, id);
            if (service != null) {
                session.remove(service);
                transaction.commit();
                System.out.println("✓ Service supprimé avec Hibernate: ID " + id);
            } else {
                System.out.println("⚠ Service non trouvé: ID " + id);
                transaction.rollback();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("✗ Erreur lors de la suppression du service: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


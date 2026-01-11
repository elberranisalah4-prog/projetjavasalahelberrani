package com.municipal.dashboard.repository;

import java.util.List;
import java.util.Optional;

/**
 * Interface générique pour la persistance des données
 * Définit les opérations CRUD de base
 * 
 * @param <T> Type d'entité à persister
 * @param <ID> Type de l'identifiant
 */
public interface Repository<T, ID> {
    
    /**
     * Sauvegarde une entité
     * @param entity Entité à sauvegarder
     * @return Entité sauvegardée
     */
    T save(T entity);
    
    /**
     * Trouve une entité par son identifiant
     * @param id Identifiant de l'entité
     * @return Optional contenant l'entité si trouvée
     */
    Optional<T> findById(ID id);
    
    /**
     * Trouve toutes les entités
     * @return Liste de toutes les entités
     */
    List<T> findAll();
    
    /**
     * Met à jour une entité
     * @param entity Entité à mettre à jour
     * @return Entité mise à jour
     */
    T update(T entity);
    
    /**
     * Supprime une entité par son identifiant
     * @param id Identifiant de l'entité à supprimer
     * @return true si supprimée, false sinon
     */
    boolean deleteById(ID id);
    
    /**
     * Compte le nombre d'entités
     * @return Nombre d'entités
     */
    long count();
    
    /**
     * Vérifie si une entité existe
     * @param id Identifiant de l'entité
     * @return true si existe, false sinon
     */
    boolean existsById(ID id);
}


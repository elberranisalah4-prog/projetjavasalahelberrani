package com.municipal.dashboard.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Gestionnaire générique pour manipuler des entités
 * Utilise les génériques pour assurer la sécurité de type
 * Le gestionnaire ne manipulera que des objets du type spécifié
 * 
 * @param <T> Type d'entité à gérer
 */
public class GenericManager<T> {
    private List<T> items;
    private Function<T, Long> idExtractor;
    
    /**
     * Constructeur avec extracteur d'ID
     * @param idExtractor Fonction pour extraire l'ID d'un élément
     */
    public GenericManager(Function<T, Long> idExtractor) {
        this.items = new ArrayList<>();
        this.idExtractor = idExtractor;
    }
    
    /**
     * Ajoute un élément au gestionnaire
     */
    public void add(T item) {
        if (item != null) {
            items.add(item);
        }
    }
    
    /**
     * Ajoute plusieurs éléments
     */
    public void addAll(List<T> items) {
        if (items != null) {
            this.items.addAll(items);
        }
    }
    
    /**
     * Trouve un élément par son ID
     */
    public T findById(Long id) {
        if (id == null || idExtractor == null) {
            return null;
        }
        return items.stream()
                .filter(item -> {
                    Long itemId = idExtractor.apply(item);
                    return itemId != null && itemId.equals(id);
                })
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Trouve tous les éléments qui satisfont un prédicat
     */
    public List<T> findAll(Predicate<T> predicate) {
        return items.stream()
                .filter(predicate)
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Supprime un élément par son ID
     */
    public boolean removeById(Long id) {
        if (id == null || idExtractor == null) {
            return false;
        }
        return items.removeIf(item -> {
            Long itemId = idExtractor.apply(item);
            return itemId != null && itemId.equals(id);
        });
    }
    
    /**
     * Met à jour un élément
     */
    public boolean update(T item) {
        if (item == null || idExtractor == null) {
            return false;
        }
        
        Long itemId = idExtractor.apply(item);
        if (itemId == null) {
            return false;
        }
        
        for (int i = 0; i < items.size(); i++) {
            Long currentId = idExtractor.apply(items.get(i));
            if (currentId != null && currentId.equals(itemId)) {
                items.set(i, item);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Retourne tous les éléments
     */
    public List<T> getAll() {
        return new ArrayList<>(items);
    }
    
    /**
     * Compte les éléments
     */
    public long count() {
        return items.size();
    }
    
    /**
     * Vérifie si un élément existe
     */
    public boolean exists(Long id) {
        return findById(id) != null;
    }
    
    /**
     * Vide le gestionnaire
     */
    public void clear() {
        items.clear();
    }
    
    /**
     * Vérifie si le gestionnaire est vide
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }
}


package com.municipal.dashboard.util;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Utilitaires pour les collections
 * Fournit des méthodes pratiques pour manipuler les collections
 */
public class CollectionUtils {
    
    /**
     * Crée une ArrayList typée
     */
    public static <T> List<T> createList() {
        return new ArrayList<>();
    }
    
    /**
     * Crée une ArrayList avec capacité initiale
     */
    public static <T> List<T> createList(int initialCapacity) {
        return new ArrayList<>(initialCapacity);
    }
    
    /**
     * Crée une ArrayList à partir d'éléments
     */
    @SafeVarargs
    public static <T> List<T> createList(T... elements) {
        List<T> list = new ArrayList<>();
        Collections.addAll(list, elements);
        return list;
    }
    
    /**
     * Crée un HashSet typé
     */
    public static <T> Set<T> createSet() {
        return new HashSet<>();
    }
    
    /**
     * Crée un HashMap typé
     */
    public static <K, V> Map<K, V> createMap() {
        return new HashMap<>();
    }
    
    /**
     * Vérifie si une collection est vide ou null
     */
    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }
    
    /**
     * Vérifie si une collection n'est pas vide
     */
    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return !isEmpty(collection);
    }
    
    /**
     * Retourne le premier élément ou null
     */
    public static <T> T getFirst(List<T> list) {
        return isEmpty(list) ? null : list.get(0);
    }
    
    /**
     * Retourne le dernier élément ou null
     */
    public static <T> T getLast(List<T> list) {
        return isEmpty(list) ? null : list.get(list.size() - 1);
    }
    
    /**
     * Convertit une collection en liste
     */
    public static <T> List<T> toList(Collection<T> collection) {
        return collection == null ? new ArrayList<>() : new ArrayList<>(collection);
    }
    
    /**
     * Convertit une collection en Set
     */
    public static <T> Set<T> toSet(Collection<T> collection) {
        return collection == null ? new HashSet<>() : new HashSet<>(collection);
    }
    
    /**
     * Convertit une liste en Map
     */
    public static <T, K, V> Map<K, V> toMap(Collection<T> collection,
                                            Function<T, K> keyMapper,
                                            Function<T, V> valueMapper) {
        if (isEmpty(collection)) {
            return new HashMap<>();
        }
        return collection.stream()
                .collect(Collectors.toMap(keyMapper, valueMapper));
    }
    
    /**
     * Filtre une collection
     */
    public static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }
        return collection.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
    
    /**
     * Transforme une collection
     */
    public static <T, R> List<R> map(Collection<T> collection, Function<T, R> mapper) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }
        return collection.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
    
    /**
     * Groupe une collection
     */
    public static <T, K> Map<K, List<T>> groupBy(Collection<T> collection, Function<T, K> keyMapper) {
        if (isEmpty(collection)) {
            return new HashMap<>();
        }
        return collection.stream()
                .collect(Collectors.groupingBy(keyMapper));
    }
    
    /**
     * Trie une collection
     */
    public static <T> List<T> sort(Collection<T> collection, Comparator<T> comparator) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }
        return collection.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
    
    /**
     * Inverse une liste
     */
    public static <T> List<T> reverse(List<T> list) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        List<T> reversed = new ArrayList<>(list);
        Collections.reverse(reversed);
        return reversed;
    }
    
    /**
     * Mélange une liste
     */
    public static <T> List<T> shuffle(List<T> list) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        List<T> shuffled = new ArrayList<>(list);
        Collections.shuffle(shuffled);
        return shuffled;
    }
    
    /**
     * Retourne une sous-liste
     */
    public static <T> List<T> subList(List<T> list, int fromIndex, int toIndex) {
        if (isEmpty(list) || fromIndex < 0 || toIndex > list.size() || fromIndex > toIndex) {
            return new ArrayList<>();
        }
        return new ArrayList<>(list.subList(fromIndex, toIndex));
    }
    
    /**
     * Retourne les N premiers éléments
     */
    public static <T> List<T> take(List<T> list, int n) {
        if (isEmpty(list) || n <= 0) {
            return new ArrayList<>();
        }
        return subList(list, 0, Math.min(n, list.size()));
    }
    
    /**
     * Retourne les éléments sauf les N premiers
     */
    public static <T> List<T> skip(List<T> list, int n) {
        if (isEmpty(list) || n <= 0) {
            return new ArrayList<>(list);
        }
        return subList(list, Math.min(n, list.size()), list.size());
    }
}


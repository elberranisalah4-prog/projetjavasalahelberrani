package com.municipal.dashboard.util;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utilitaires pour les opérations sur streams Java 8+
 * Fournit des méthodes pratiques pour traiter les collections de données
 */
public class StreamUtils {
    
    /**
     * Filtre une collection selon un prédicat
     */
    public static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {
        return collection.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
    
    /**
     * Transforme une collection en utilisant un mapper
     */
    public static <T, R> List<R> map(Collection<T> collection, Function<T, R> mapper) {
        return collection.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
    
    /**
     * Trie une collection selon un comparateur
     */
    public static <T> List<T> sort(Collection<T> collection, Comparator<T> comparator) {
        return collection.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
    
    /**
     * Groupe une collection par une clé
     */
    public static <T, K> Map<K, List<T>> groupBy(Collection<T> collection, Function<T, K> keyMapper) {
        return collection.stream()
                .collect(Collectors.groupingBy(keyMapper));
    }
    
    /**
     * Calcule la somme d'une collection numérique
     */
    public static <T> Double sum(Collection<T> collection, Function<T, Double> valueExtractor) {
        return collection.stream()
                .map(valueExtractor)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .sum();
    }
    
    /**
     * Calcule la moyenne d'une collection numérique
     */
    public static <T> OptionalDouble average(Collection<T> collection, Function<T, Double> valueExtractor) {
        return collection.stream()
                .map(valueExtractor)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average();
    }
    
    /**
     * Trouve la valeur maximale
     */
    public static <T> Optional<T> max(Collection<T> collection, Comparator<T> comparator) {
        return collection.stream()
                .max(comparator);
    }
    
    /**
     * Trouve la valeur minimale
     */
    public static <T> Optional<T> min(Collection<T> collection, Comparator<T> comparator) {
        return collection.stream()
                .min(comparator);
    }
    
    /**
     * Compte les éléments selon un prédicat
     */
    public static <T> long count(Collection<T> collection, Predicate<T> predicate) {
        return collection.stream()
                .filter(predicate)
                .count();
    }
    
    /**
     * Vérifie si tous les éléments satisfont un prédicat
     */
    public static <T> boolean allMatch(Collection<T> collection, Predicate<T> predicate) {
        return collection.stream()
                .allMatch(predicate);
    }
    
    /**
     * Vérifie si au moins un élément satisfait un prédicat
     */
    public static <T> boolean anyMatch(Collection<T> collection, Predicate<T> predicate) {
        return collection.stream()
                .anyMatch(predicate);
    }
    
    /**
     * Trouve le premier élément qui satisfait un prédicat
     */
    public static <T> Optional<T> findFirst(Collection<T> collection, Predicate<T> predicate) {
        return collection.stream()
                .filter(predicate)
                .findFirst();
    }
    
    /**
     * Limite le nombre d'éléments
     */
    public static <T> List<T> limit(Collection<T> collection, long maxSize) {
        return collection.stream()
                .limit(maxSize)
                .collect(Collectors.toList());
    }
    
    /**
     * Ignore les N premiers éléments
     */
    public static <T> List<T> skip(Collection<T> collection, long n) {
        return collection.stream()
                .skip(n)
                .collect(Collectors.toList());
    }
    
    /**
     * Combine deux collections
     */
    public static <T> List<T> concat(Collection<T> collection1, Collection<T> collection2) {
        return Stream.concat(collection1.stream(), collection2.stream())
                .collect(Collectors.toList());
    }
    
    /**
     * Supprime les doublons
     */
    public static <T> List<T> distinct(Collection<T> collection) {
        return collection.stream()
                .distinct()
                .collect(Collectors.toList());
    }
    
    /**
     * Collecte dans un Set
     */
    public static <T> Set<T> toSet(Collection<T> collection) {
        return collection.stream()
                .collect(Collectors.toSet());
    }
    
    /**
     * Collecte dans une Map
     */
    public static <T, K, V> Map<K, V> toMap(Collection<T> collection,
                                            Function<T, K> keyMapper,
                                            Function<T, V> valueMapper) {
        return collection.stream()
                .collect(Collectors.toMap(keyMapper, valueMapper));
    }
    
    /**
     * Partitionne selon un prédicat
     */
    public static <T> Map<Boolean, List<T>> partition(Collection<T> collection, Predicate<T> predicate) {
        return collection.stream()
                .collect(Collectors.partitioningBy(predicate));
    }
    
    /**
     * Agrège des valeurs numériques par groupe
     */
    public static <T, K> Map<K, Double> sumBy(Collection<T> collection,
                                               Function<T, K> keyMapper,
                                               Function<T, Double> valueExtractor) {
        return collection.stream()
                .collect(Collectors.groupingBy(
                    keyMapper,
                    Collectors.summingDouble(valueExtractor::apply)
                ));
    }
    
    /**
     * Calcule la moyenne par groupe
     */
    public static <T, K> Map<K, Double> averageBy(Collection<T> collection,
                                                  Function<T, K> keyMapper,
                                                  Function<T, Double> valueExtractor) {
        return collection.stream()
                .collect(Collectors.groupingBy(
                    keyMapper,
                    Collectors.averagingDouble(valueExtractor::apply)
                ));
    }
    
    /**
     * Filtre et transforme en une seule opération
     */
    public static <T, R> List<R> filterAndMap(Collection<T> collection,
                                                Predicate<T> filter,
                                                Function<T, R> mapper) {
        return collection.stream()
                .filter(filter)
                .map(mapper)
                .collect(Collectors.toList());
    }
    
    /**
     * Traite en parallèle pour de grandes collections
     */
    public static <T, R> List<R> parallelMap(Collection<T> collection, Function<T, R> mapper) {
        return collection.parallelStream()
                .map(mapper)
                .collect(Collectors.toList());
    }
}



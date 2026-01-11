package com.municipal.dashboard.util;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Utilitaires pour Optional
 * Aide à gérer l'absence de valeurs de manière élégante
 */
public class OptionalUtils {
    
    /**
     * Crée un Optional à partir d'une valeur nullable
     */
    public static <T> Optional<T> ofNullable(T value) {
        return Optional.ofNullable(value);
    }
    
    /**
     * Retourne une valeur par défaut si Optional est vide
     */
    public static <T> T orElse(Optional<T> optional, T defaultValue) {
        return optional.orElse(defaultValue);
    }
    
    /**
     * Retourne une valeur calculée si Optional est vide
     */
    public static <T> T orElseGet(Optional<T> optional, Supplier<T> supplier) {
        return optional.orElseGet(supplier);
    }
    
    /**
     * Lance une exception si Optional est vide
     */
    public static <T> T orElseThrow(Optional<T> optional, Supplier<? extends RuntimeException> exceptionSupplier) {
        return optional.orElseThrow(exceptionSupplier);
    }
    
    /**
     * Applique une fonction si Optional n'est pas vide
     */
    public static <T, R> Optional<R> map(Optional<T> optional, Function<T, R> mapper) {
        return optional.map(mapper);
    }
    
    /**
     * Applique une fonction qui retourne un Optional
     */
    public static <T, R> Optional<R> flatMap(Optional<T> optional, Function<T, Optional<R>> mapper) {
        return optional.flatMap(mapper);
    }
    
    /**
     * Exécute une action si Optional n'est pas vide
     */
    public static <T> void ifPresent(Optional<T> optional, java.util.function.Consumer<T> action) {
        optional.ifPresent(action);
    }
    
    /**
     * Exécute une action si Optional est vide
     */
    public static <T> void ifEmpty(Optional<T> optional, Runnable action) {
        if (!optional.isPresent()) {
            action.run();
        }
    }
    
    /**
     * Exécute une action si présent, sinon une autre
     */
    public static <T> void ifPresentOrElse(Optional<T> optional,
                                           java.util.function.Consumer<T> ifPresent,
                                           Runnable ifEmpty) {
        if (optional.isPresent()) {
            ifPresent.accept(optional.get());
        } else {
            ifEmpty.run();
        }
    }
    
    /**
     * Filtre un Optional selon un prédicat
     */
    public static <T> Optional<T> filter(Optional<T> optional, java.util.function.Predicate<T> predicate) {
        return optional.filter(predicate);
    }
    
    /**
     * Retourne le premier Optional non vide
     */
    @SafeVarargs
    public static <T> Optional<T> firstPresent(Optional<T>... optionals) {
        for (Optional<T> optional : optionals) {
            if (optional.isPresent()) {
                return optional;
            }
        }
        return Optional.empty();
    }
    
    /**
     * Convertit une valeur nullable en Optional avec vérification
     */
    public static <T> Optional<T> safeOf(T value) {
        return value == null ? Optional.empty() : Optional.of(value);
    }
    
    /**
     * Retourne une valeur ou null si Optional est vide
     */
    public static <T> T orNull(Optional<T> optional) {
        return optional.orElse(null);
    }
    
    /**
     * Retourne une chaîne vide si Optional est vide
     */
    public static String orEmptyString(Optional<String> optional) {
        return optional.orElse("");
    }
    
    /**
     * Retourne 0 si Optional est vide (pour nombres)
     */
    public static <T extends Number> T orZero(Optional<T> optional, Supplier<T> zeroSupplier) {
        return optional.orElseGet(zeroSupplier);
    }
}


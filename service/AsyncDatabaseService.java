package com.municipal.dashboard.service;

import com.municipal.dashboard.util.ThreadPoolManager;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Service pour exécuter des opérations de base de données de manière asynchrone
 * Utilise ThreadPoolManager pour gérer les threads
 */
public class AsyncDatabaseService {
    private ThreadPoolManager threadPoolManager;
    
    public AsyncDatabaseService() {
        this.threadPoolManager = ThreadPoolManager.getInstance();
    }
    
    /**
     * Exécute une opération de lecture de manière asynchrone
     */
    public <T> CompletableFuture<T> readAsync(Supplier<T> databaseOperation) {
        return threadPoolManager.executeAsync(() -> {
            try {
                return databaseOperation.get();
            } catch (Exception e) {
                System.err.println("Erreur lors de l'opération asynchrone: " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }
    
    /**
     * Exécute une opération de lecture avec callback
     */
    public <T> void readAsync(Supplier<T> databaseOperation,
                              Consumer<T> onSuccess,
                              Consumer<Exception> onError) {
        threadPoolManager.executeAsync(
            () -> databaseOperation.get(),
            onSuccess,
            onError
        );
    }
    
    /**
     * Exécute une opération d'écriture de manière asynchrone
     */
    public CompletableFuture<Void> writeAsync(Runnable databaseOperation) {
        return threadPoolManager.executeAsync(() -> {
            try {
                databaseOperation.run();
            } catch (Exception e) {
                System.err.println("Erreur lors de l'opération d'écriture: " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }
    
    /**
     * Exécute une opération d'écriture avec callback
     */
    public void writeAsync(Runnable databaseOperation,
                          Runnable onSuccess,
                          Consumer<Exception> onError) {
        threadPoolManager.executeAsync(
            () -> {
                databaseOperation.run();
                return null;
            },
            result -> {
                if (onSuccess != null) {
                    onSuccess.run();
                }
            },
            onError
        );
    }
    
    /**
     * Exécute plusieurs opérations en parallèle
     */
    @SafeVarargs
    public final <T> CompletableFuture<List<T>> executeAllAsync(Supplier<T>... operations) {
        @SuppressWarnings("unchecked")
        CompletableFuture<T>[] futures = new CompletableFuture[operations.length];
        
        for (int i = 0; i < operations.length; i++) {
            futures[i] = readAsync(operations[i]);
        }
        
        return CompletableFuture.allOf(futures)
                .thenApply(v -> {
                    List<T> results = new java.util.ArrayList<>();
                    for (CompletableFuture<T> future : futures) {
                        try {
                            results.add(future.get());
                        } catch (Exception e) {
                            System.err.println("Erreur lors de la récupération du résultat: " + e.getMessage());
                        }
                    }
                    return results;
                });
    }
    
    /**
     * Exécute une opération avec retry automatique
     */
    public <T> CompletableFuture<T> readWithRetry(Supplier<T> operation, int maxRetries) {
        return CompletableFuture.supplyAsync(() -> {
            Exception lastException = null;
            for (int i = 0; i < maxRetries; i++) {
                try {
                    return operation.get();
                } catch (Exception e) {
                    lastException = e;
                    System.out.println("Tentative " + (i + 1) + "/" + maxRetries + " échouée: " + e.getMessage());
                    if (i < maxRetries - 1) {
                        try {
                            Thread.sleep(1000 * (i + 1)); // Délai exponentiel
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            throw new RuntimeException(ie);
                        }
                    }
                }
            }
            throw new RuntimeException("Échec après " + maxRetries + " tentatives", lastException);
        }, ThreadPoolManager.getInstance().getExecutorService());
    }
}



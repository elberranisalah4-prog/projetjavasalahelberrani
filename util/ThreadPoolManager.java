package com.municipal.dashboard.util;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Gestionnaire de pool de threads pour les opérations asynchrones
 * Utilise ExecutorService pour gérer efficacement les threads
 */
public class ThreadPoolManager {
    private static ThreadPoolManager instance;
    private ExecutorService executorService;
    private ScheduledExecutorService scheduledExecutorService;
    private final int CORE_POOL_SIZE = 5;
    private final int MAX_POOL_SIZE = 10;
    private final long KEEP_ALIVE_TIME = 60L;
    
    private ThreadPoolManager() {
        initializeThreadPools();
    }
    
    public static synchronized ThreadPoolManager getInstance() {
        if (instance == null) {
            instance = new ThreadPoolManager();
        }
        return instance;
    }
    
    private void initializeThreadPools() {
        // Pool de threads pour les opérations générales
        executorService = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            new CustomThreadFactory("Dashboard-Thread")
        );
        
        // Pool de threads pour les tâches planifiées
        scheduledExecutorService = Executors.newScheduledThreadPool(
            3,
            new CustomThreadFactory("Dashboard-Scheduled")
        );
        
        System.out.println("✓ ThreadPoolManager initialisé");
        System.out.println("  - Pool principal: " + CORE_POOL_SIZE + "-" + MAX_POOL_SIZE + " threads");
        System.out.println("  - Pool planifié: 3 threads");
    }
    
    /**
     * Obtient l'ExecutorService pour utilisation directe si nécessaire
     */
    public ExecutorService getExecutorService() {
        return executorService;
    }
    
    /**
     * Exécute une tâche de manière asynchrone
     */
    public <T> CompletableFuture<T> executeAsync(Callable<T> task) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return task.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, executorService);
    }
    
    /**
     * Exécute une tâche Runnable de manière asynchrone
     */
    public CompletableFuture<Void> executeAsync(Runnable task) {
        return CompletableFuture.runAsync(task, executorService);
    }
    
    /**
     * Exécute une tâche avec un callback
     */
    public <T> void executeAsync(Callable<T> task, 
                                  java.util.function.Consumer<T> onSuccess,
                                  java.util.function.Consumer<Exception> onError) {
        CompletableFuture.supplyAsync(() -> {
            try {
                return task.call();
            } catch (Exception e) {
                if (onError != null) {
                    onError.accept(e);
                }
                throw new RuntimeException(e);
            }
        }, executorService).thenAccept(result -> {
            if (onSuccess != null) {
                onSuccess.accept(result);
            }
        }).exceptionally(throwable -> {
            if (onError != null && throwable.getCause() instanceof Exception) {
                onError.accept((Exception) throwable.getCause());
            }
            return null;
        });
    }
    
    /**
     * Planifie une tâche pour exécution périodique
     */
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, 
                                                   long initialDelay, 
                                                   long period, 
                                                   TimeUnit unit) {
        return scheduledExecutorService.scheduleAtFixedRate(task, initialDelay, period, unit);
    }
    
    /**
     * Planifie une tâche avec délai
     */
    public ScheduledFuture<?> schedule(Runnable task, long delay, TimeUnit unit) {
        return scheduledExecutorService.schedule(task, delay, unit);
    }
    
    /**
     * Obtient les statistiques du pool de threads
     */
    public String getPoolStats() {
        if (executorService instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor tpe = (ThreadPoolExecutor) executorService;
            return String.format(
                "Pool Stats - Active: %d, Pool: %d, Queue: %d, Completed: %d",
                tpe.getActiveCount(),
                tpe.getPoolSize(),
                tpe.getQueue().size(),
                tpe.getCompletedTaskCount()
            );
        }
        return "Pool Stats - N/A";
    }
    
    /**
     * Arrête proprement tous les pools de threads
     */
    public void shutdown() {
        System.out.println("Arrêt du ThreadPoolManager...");
        executorService.shutdown();
        scheduledExecutorService.shutdown();
        
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
            if (!scheduledExecutorService.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduledExecutorService.shutdownNow();
            }
            System.out.println("✓ ThreadPoolManager arrêté");
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            scheduledExecutorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Factory personnalisée pour créer des threads avec des noms significatifs
     */
    private static class CustomThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;
        
        CustomThreadFactory(String namePrefix) {
            this.namePrefix = namePrefix;
        }
        
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, namePrefix + "-" + threadNumber.getAndIncrement());
            t.setDaemon(false);
            return t;
        }
    }
}



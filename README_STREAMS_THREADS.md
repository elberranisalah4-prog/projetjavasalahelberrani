# Guide d'utilisation: Streams et Threads

Ce projet intègre des fonctionnalités avancées de **Java Streams** et **Threads** pour améliorer les performances et la réactivité de l'application.

## 📚 Architecture

### 1. ThreadPoolManager (`util/ThreadPoolManager.java`)
Gestionnaire centralisé de pool de threads pour toutes les opérations asynchrones.

**Fonctionnalités:**
- Pool de threads configurable (5-10 threads)
- Pool de threads planifiés pour les tâches périodiques
- Gestion automatique du cycle de vie des threads
- Statistiques du pool

**Utilisation:**
```java
ThreadPoolManager manager = ThreadPoolManager.getInstance();

// Exécuter une tâche asynchrone
CompletableFuture<String> future = manager.executeAsync(() -> {
    return "Résultat";
});

// Avec callback
manager.executeAsync(
    () -> operation(),
    result -> System.out.println("Succès: " + result),
    error -> System.err.println("Erreur: " + error)
);
```

### 2. StreamUtils (`util/StreamUtils.java`)
Collection d'utilitaires pour les opérations sur streams Java 8+.

**Fonctionnalités:**
- Filtrage, mapping, tri
- Groupement et agrégation
- Calculs statistiques (somme, moyenne, max, min)
- Opérations parallèles

**Exemples:**
```java
// Filtrer
List<Consommation> grandes = StreamUtils.filter(
    consommations,
    c -> c.getValeur() > 100
);

// Grouper
Map<String, List<Consommation>> parZone = StreamUtils.groupBy(
    consommations,
    Consommation::getZone
);

// Calculer la somme
Double total = StreamUtils.sum(consommations, Consommation::getValeur);

// Traitement parallèle
List<Result> results = StreamUtils.parallelMap(data, mapper);
```

### 3. AsyncDatabaseService (`service/AsyncDatabaseService.java`)
Service pour exécuter les opérations de base de données de manière asynchrone.

**Fonctionnalités:**
- Lecture asynchrone
- Écriture asynchrone
- Exécution parallèle de plusieurs opérations
- Retry automatique

**Exemples:**
```java
AsyncDatabaseService service = new AsyncDatabaseService();

// Lecture asynchrone
CompletableFuture<List<Consommation>> future = service.readAsync(
    () -> consommationDAO.findAll()
);

// Avec callback
service.readAsync(
    () -> consommationDAO.findAll(),
    consommations -> System.out.println("Chargé: " + consommations.size()),
    error -> System.err.println("Erreur: " + error)
);

// Plusieurs opérations en parallèle
CompletableFuture<List<Object>> all = service.executeAllAsync(
    () -> consommationDAO.findAll(),
    () -> productionDAO.findAll()
);
```

### 4. DataStreamService (`service/DataStreamService.java`)
Service spécialisé pour analyser les données avec des streams.

**Fonctionnalités:**
- Statistiques par zone
- Filtrage par période
- Détection d'anomalies
- Top N éléments

**Exemples:**
```java
DataStreamService streamService = new DataStreamService();

// Statistiques par zone
Map<String, Double> stats = streamService.getConsommationByZone(consommations);

// Top 10
List<Consommation> top10 = streamService.getTopConsommations(consommations, 10);

// Statistiques complètes
Map<String, Object> stats = streamService.getStatistics(consommations);
```

## 🚀 Exemples d'utilisation

### Exemple 1: Chargement asynchrone avec traitement stream

```java
ConsommationDAOAsync dao = new ConsommationDAOAsync();

dao.findAllAsync()
    .thenApply(consommations -> {
        // Traitement avec streams
        return StreamUtils.filter(
            consommations,
            c -> c.getValeur() > 100
        );
    })
    .thenAccept(filtered -> {
        System.out.println("Trouvé: " + filtered.size());
    });
```

### Exemple 2: Calculs statistiques asynchrones

```java
ConsommationDAOAsync dao = new ConsommationDAOAsync();

// Calculer plusieurs statistiques en parallèle
CompletableFuture<Double> total = dao.getTotalAsync();
CompletableFuture<Double> moyenne = dao.getAverageAsync();
CompletableFuture<Map<String, Double>> parZone = dao.getStatisticsByZoneAsync();

CompletableFuture.allOf(total, moyenne, parZone)
    .thenRun(() -> {
        System.out.println("Total: " + total.join());
        System.out.println("Moyenne: " + moyenne.join());
        parZone.join().forEach((zone, somme) -> 
            System.out.println(zone + ": " + somme)
        );
    });
```

### Exemple 3: Filtrage et tri avec streams

```java
List<Consommation> resultats = consommations.stream()
    .filter(c -> c.getValeur() != null && c.getValeur() > 50)
    .filter(c -> "Zone A".equals(c.getZone()))
    .sorted(Comparator.comparing(Consommation::getDate).reversed())
    .limit(10)
    .collect(Collectors.toList());
```

### Exemple 4: Traitement parallèle

```java
// Traitement parallèle pour de grandes collections
List<ProcessedData> processed = StreamUtils.parallelMap(
    largeCollection,
    item -> heavyProcessing(item)
);
```

## 📊 Avantages

### Performance
- **Parallélisation**: Traitement simultané de plusieurs opérations
- **Non-bloquant**: L'interface reste réactive pendant les opérations
- **Efficacité**: Utilisation optimale des ressources CPU

### Code propre
- **Fonctionnel**: Style de programmation déclaratif
- **Lisible**: Code plus expressif et facile à comprendre
- **Maintenable**: Séparation claire des responsabilités

### Scalabilité
- **Pool de threads**: Gestion automatique des ressources
- **Streams parallèles**: Traitement efficace de grandes collections
- **Asynchrone**: Pas de blocage de l'interface utilisateur

## 🔧 Configuration

### ThreadPoolManager
Par défaut:
- Pool principal: 5-10 threads
- Pool planifié: 3 threads
- Timeout: 60 secondes

Pour modifier, éditez `ThreadPoolManager.java`:
```java
private final int CORE_POOL_SIZE = 5;
private final int MAX_POOL_SIZE = 10;
```

## 📝 Bonnes pratiques

1. **Toujours gérer les erreurs** dans les callbacks
2. **Fermer le ThreadPoolManager** à la fin de l'application
3. **Utiliser streams parallèles** uniquement pour grandes collections
4. **Éviter les opérations bloquantes** dans les threads

## 🧪 Tests

Exécutez les exemples:
```bash
javac -cp "lib/mysql-connector-java.jar" com/municipal/dashboard/exemple/ExempleStreamsThreads.java
java -cp ".;lib/mysql-connector-java.jar" com.municipal.dashboard.exemple.ExempleStreamsThreads
```

## 📚 Ressources

- [Java Streams Documentation](https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html)
- [CompletableFuture Documentation](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)
- [ExecutorService Documentation](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html)



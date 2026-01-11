# Guide des Concepts Clés Java

Ce projet intègre les **4 concepts clés** de la programmation Java moderne pour assurer un code robuste, maintenable et performant.

## 📚 Les 4 Concepts Clés

### 1. 🔷 Génériques (`<T extends Produit>`)

**Objectif:** Assurer la sécurité de type. Le gestionnaire ne manipulera que des objets du type spécifié.

**Avantages:**
- ✅ Détection d'erreurs à la compilation
- ✅ Pas de cast nécessaire
- ✅ Code plus lisible et maintenable

**Exemple dans le projet:**
```java
// Gestionnaire générique pour Consommation
GenericManager<Consommation> manager = new GenericManager<>(Consommation::getId);

// Seules les Consommation peuvent être ajoutées
manager.add(new Consommation(100.0, LocalDateTime.now(), "Eau", "Zone A"));

// Le compilateur empêche les erreurs de type
// manager.add("Ceci ne compile pas!"); // ❌ ERREUR
```

**Fichier:** `util/GenericManager.java`

---

### 2. 📁 Collections (`ArrayList<T>`)

**Objectif:** Stockage volatile des données. Rapide et simple, mais sans persistance.

**Types disponibles:**
- `List<T>` - Liste ordonnée avec doublons
- `Set<T>` - Ensemble sans doublons
- `Map<K, V>` - Association clé-valeur

**Exemple dans le projet:**
```java
// Création de collections typées
List<Consommation> consommations = CollectionUtils.createList();
Set<String> zones = CollectionUtils.createSet();
Map<String, Double> stats = CollectionUtils.createMap();

// Manipulation
consommations.add(new Consommation(...));
zones.addAll(CollectionUtils.map(consommations, Consommation::getZone));

// Groupement
Map<String, List<Consommation>> parZone = CollectionUtils.groupBy(
    consommations, 
    Consommation::getZone
);
```

**Fichier:** `util/CollectionUtils.java`

---

### 3. 🌊 Streams & Lambdas (`stream().filter()`)

**Objectif:** Code déclaratif, lisible et puissant pour la recherche et le traitement.

**Avantages:**
- ✅ Code fonctionnel et expressif
- ✅ Traitement en pipeline
- ✅ Opérations parallèles possibles

**Exemple dans le projet:**
```java
// Filtrage
List<Consommation> grandes = consommations.stream()
    .filter(c -> c.getValeur() != null && c.getValeur() > 100)
    .collect(Collectors.toList());

// Transformation
List<String> zones = consommations.stream()
    .map(Consommation::getZone)
    .distinct()
    .collect(Collectors.toList());

// Agrégation
Map<String, Double> sommeParZone = consommations.stream()
    .filter(c -> c.getZone() != null && c.getValeur() != null)
    .collect(Collectors.groupingBy(
        Consommation::getZone,
        Collectors.summingDouble(Consommation::getValeur)
    ));

// Recherche
consommations.stream()
    .filter(c -> "Zone A".equals(c.getZone()))
    .findFirst()
    .ifPresent(c -> System.out.println("Trouvé: " + c.getValeur()));
```

**Fichiers:** 
- `util/StreamUtils.java`
- `service/DataStreamService.java`

---

### 4. 📦 Optional (`Optional<T>`)

**Objectif:** Gérer l'absence de résultat de manière élégante et éviter les `NullPointerException`.

**Avantages:**
- ✅ Pas de `NullPointerException`
- ✅ Code explicite sur l'absence de valeur
- ✅ Chaînage fluide

**Exemple dans le projet:**
```java
// Recherche avec Optional
Optional<Consommation> consommation = OptionalUtils.ofNullable(dao.findById(1L));

// Méthode 1: ifPresent
consommation.ifPresent(c -> 
    System.out.println("Trouvé: " + c.getValeur())
);

// Méthode 2: orElse
Consommation result = consommation.orElse(
    new Consommation(0.0, LocalDateTime.now(), "Défaut", "Zone")
);

// Méthode 3: Chaînage avec map
Optional<String> zone = consommation
    .map(Consommation::getZone)
    .map(String::toUpperCase);

// Méthode 4: Filtrage
consommation
    .filter(c -> c.getValeur() != null && c.getValeur() > 100)
    .ifPresent(c -> System.out.println("> 100: " + c.getValeur()));

// Gestion sécurisée des null
String zoneSafe = OptionalUtils.ofNullable(consommation)
    .map(Consommation::getZone)
    .orElse("Zone inconnue");
```

**Fichier:** `util/OptionalUtils.java`

---

## 🎯 Utilisation Combinée

Les concepts fonctionnent parfaitement ensemble:

```java
// 1. Génériques: Gestionnaire typé
GenericManager<Consommation> manager = new GenericManager<>(Consommation::getId);

// 2. Collections: Stockage
List<Consommation> consommations = dao.findAll();
manager.addAll(consommations);

// 3. Streams & Lambdas: Traitement déclaratif
Map<String, Double> stats = consommations.stream()
    .filter(c -> c.getValeur() != null && c.getZone() != null)
    .collect(Collectors.groupingBy(
        Consommation::getZone,
        Collectors.summingDouble(Consommation::getValeur)
    ));

// 4. Optional: Gestion sécurisée
stats.entrySet().stream()
    .max(Map.Entry.comparingByValue())
    .ifPresent(entry -> {
        System.out.println("Zone max: " + entry.getKey() + " = " + entry.getValue());
    });
```

---

## 📖 Exemples Complets

### Exemple 1: Recherche avec tous les concepts

```java
// Génériques + Collections + Streams + Optional
GenericManager<Consommation> manager = new GenericManager<>(Consommation::getId);
manager.addAll(dao.findAll());

Optional<Consommation> maxConsommation = manager.getAll().stream()
    .filter(c -> c.getValeur() != null)
    .max(Comparator.comparing(Consommation::getValeur));

maxConsommation.ifPresent(c -> {
    System.out.println("Max: " + c.getValeur());
    System.out.println("Zone: " + OptionalUtils.ofNullable(c.getZone()).orElse("Inconnue"));
});
```

### Exemple 2: Statistiques par zone

```java
// Utilisation combinée
List<Consommation> consommations = dao.findAll();

Map<String, Double> stats = consommations.stream()
    .filter(c -> c.getZone() != null && c.getValeur() != null)
    .collect(Collectors.groupingBy(
        Consommation::getZone,
        Collectors.summingDouble(Consommation::getValeur)
    ));

stats.forEach((zone, somme) -> 
    System.out.println(zone + ": " + somme)
);
```

---

## 🧪 Tests

Exécutez les exemples complets:

```bash
TESTER_CONCEPTS_CLES.bat
```

Ou manuellement:
```bash
javac -cp "lib/mysql-connector-java.jar" com/municipal/dashboard/exemple/ExempleConceptsCles.java
java -cp ".;lib/mysql-connector-java.jar" com.municipal.dashboard.exemple.ExempleConceptsCles
```

---

## 📁 Structure des Fichiers

```
util/
├── GenericManager.java      # Gestionnaire générique
├── CollectionUtils.java     # Utilitaires pour collections
├── OptionalUtils.java       # Utilitaires pour Optional
└── StreamUtils.java         # Utilitaires pour streams

exemple/
└── ExempleConceptsCles.java # Exemples complets
```

---

## ✅ Bonnes Pratiques

### Génériques
- ✅ Utilisez toujours des types génériques pour les collections
- ✅ Évitez les `@SuppressWarnings("unchecked")`
- ✅ Préférez les génériques aux casts

### Collections
- ✅ Choisissez le bon type (List vs Set vs Map)
- ✅ Utilisez `Collections.emptyList()` pour les listes vides
- ✅ Préférez `ArrayList` pour les accès aléatoires

### Streams
- ✅ Utilisez des streams pour le traitement de données
- ✅ Préférez les méthodes de référence (`Consommation::getZone`)
- ✅ Utilisez `parallelStream()` pour grandes collections

### Optional
- ✅ Utilisez `Optional` pour les valeurs potentiellement nulles
- ✅ Évitez `Optional.get()` sans vérification
- ✅ Préférez `orElseGet()` à `orElse()` pour les calculs coûteux

---

## 🎓 Ressources

- [Java Generics Tutorial](https://docs.oracle.com/javase/tutorial/java/generics/)
- [Java Collections Framework](https://docs.oracle.com/javase/tutorial/collections/)
- [Java Streams API](https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html)
- [Java Optional](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html)

---

## 🚀 Avantages dans le Projet

1. **Sécurité de type** - Moins d'erreurs à l'exécution
2. **Code propre** - Plus lisible et maintenable
3. **Performance** - Traitement efficace des données
4. **Robustesse** - Gestion élégante des cas limites

Tous ces concepts sont intégrés et utilisables dans votre projet municipal! 🎉


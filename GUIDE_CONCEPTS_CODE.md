# Guide des Concepts avec Exemples de Code

Ce document explique les concepts clés utilisés dans le projet avec des exemples de code concrets.

---

## 1. JDBC (Java Database Connectivity)

**Concept :** JDBC est l'API Java standard pour se connecter à une base de données relationnelle et exécuter des requêtes SQL.

**Code exemple dans le projet :**

```java
// Fichier: DatabaseManager.java

// 1. Chargement du driver MySQL
Class.forName("com.mysql.cj.jdbc.Driver");

// 2. Connexion à la base de données
String url = "jdbc:mysql://localhost:3306/services_publics";
Connection connection = DriverManager.getConnection(url, "root", "");

// 3. Exécution d'une requête SELECT
String sql = "SELECT * FROM service ORDER BY nom";
Statement stmt = connection.createStatement();
ResultSet rs = stmt.executeQuery(sql);

while (rs.next()) {
    Long id = rs.getLong("id");
    String nom = rs.getString("nom");
    System.out.println("ID: " + id + ", Nom: " + nom);
}

// 4. Exécution d'une requête INSERT avec PreparedStatement
String insertSQL = "INSERT INTO service (type, nom, description) VALUES (?, ?, ?)";
PreparedStatement pstmt = connection.prepareStatement(insertSQL);
pstmt.setString(1, "Eau");
pstmt.setString(2, "Distribution d'eau");
pstmt.setString(3, "Service de distribution d'eau potable");
pstmt.executeUpdate();

// 5. Fermeture des ressources
rs.close();
stmt.close();
connection.close();
```

**Avantages :**
- Contrôle total sur les requêtes SQL
- Performance maximale
- Flexibilité complète

**Inconvénients :**
- Plus de code à écrire
- Gestion manuelle des transactions
- Mapping manuel des résultats

---

## 2. Hibernate (ORM - Object-Relational Mapping)

**Concept :** Hibernate est un framework ORM qui mappe automatiquement les objets Java aux tables de base de données, évitant d'écrire du SQL manuellement.

**Code exemple dans le projet :**

```java
// Fichier: ServiceDAOHibernate.java

// 1. Configuration (hibernate.cfg.xml)
// <?xml version="1.0"?>
// <hibernate-configuration>
//   <session-factory>
//     <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/services_publics</property>
//     <property name="hibernate.dialect">org.hibernate.dialect.MySQL8Dialect</property>
//   </session-factory>
// </hibernate-configuration>

// 2. Entité annotée
@Entity
@Table(name = "service")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nom", nullable = false)
    private String nom;
    
    // Getters et setters...
}

// 3. Utilisation de Hibernate
SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
Session session = sessionFactory.openSession();
Transaction transaction = session.beginTransaction();

// Insérer un service
Service service = new Service("Eau", "Distribution d'eau", "Description");
session.persist(service);  // Pas besoin de SQL !

// Récupérer tous les services
List<Service> services = session.createQuery("FROM Service", Service.class).getResultList();

// Trouver par ID
Service s = session.get(Service.class, 1L);

// Mettre à jour
service.setNom("Nouveau nom");
session.merge(service);

// Supprimer
session.remove(service);

transaction.commit();
session.close();
```

**Avantages :**
- Moins de code
- Mapping automatique
- Gestion automatique des transactions
- Cache intégré

**Inconvénients :**
- Moins de contrôle sur les requêtes
- Courbe d'apprentissage
- Peut être moins performant pour des requêtes complexes

---

## 3. Thread (Threads et Programmation Concurrente)

**Concept :** Un thread permet d'exécuter plusieurs tâches en parallèle dans une application Java.

**Code exemple dans le projet :**

```java
// Fichier: ThreadPoolManager.java

// 1. Pool de threads avec ExecutorService
ExecutorService executorService = Executors.newFixedThreadPool(5);

// 2. Exécution d'une tâche asynchrone
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    // Tâche qui s'exécute dans un thread séparé
    try {
        Thread.sleep(1000); // Simulation d'une tâche longue
        return "Résultat de la tâche asynchrone";
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        return "Erreur";
    }
}, executorService);

// 3. Utilisation du résultat (non-bloquant)
future.thenAccept(result -> {
    System.out.println("Résultat reçu: " + result);
});

// 4. Exécution avec callback
executorService.execute(() -> {
    System.out.println("Tâche exécutée dans un thread séparé");
});

// 5. Tâches planifiées (récurrentes)
ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(3);
scheduledExecutor.scheduleAtFixedRate(() -> {
    System.out.println("Tâche exécutée toutes les 5 secondes");
}, 0, 5, TimeUnit.SECONDS);

// 6. Arrêt propre des threads
executorService.shutdown();
try {
    if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
        executorService.shutdownNow();
    }
} catch (InterruptedException e) {
    executorService.shutdownNow();
}
```

**Cas d'usage dans le projet :**
- Opérations de base de données asynchrones
- Mises à jour périodiques des données
- Traitement parallèle de grandes quantités de données

---

## 4. Stream (Java Streams API)

**Concept :** Les Streams permettent de traiter des collections de données de manière fonctionnelle et déclarative.

**Code exemple dans le projet :**

```java
// Fichier: ExempleStreamsThreads.java (ou dans le code)

import java.util.*;
import java.util.stream.*;
import java.util.function.*;

// 1. Filtrer et transformer une liste
List<Service> services = serviceDAO.findAll();

// Filtrer les services par type
List<Service> servicesEau = services.stream()
    .filter(s -> s.getType().equals("Eau"))
    .collect(Collectors.toList());

// 2. Transformer (map) une liste
List<String> nomsServices = services.stream()
    .map(Service::getNom)  // Récupère seulement les noms
    .collect(Collectors.toList());

// 3. Trier une liste
List<Service> servicesTries = services.stream()
    .sorted(Comparator.comparing(Service::getNom))
    .collect(Collectors.toList());

// 4. Rechercher et compter
long count = services.stream()
    .filter(s -> s.getType().equals("Eau"))
    .count();

Optional<Service> premierService = services.stream()
    .filter(s -> s.getNom().contains("eau"))
    .findFirst();

// 5. Regrouper (groupBy)
Map<String, List<Service>> servicesParType = services.stream()
    .collect(Collectors.groupingBy(Service::getType));

// 6. Opérations sur les nombres
List<Consommation> consommations = consommationDAO.findAll();
double moyenne = consommations.stream()
    .mapToDouble(Consommation::getValeur)
    .average()
    .orElse(0.0);

double somme = consommations.stream()
    .mapToDouble(Consommation::getValeur)
    .sum();

// 7. Stream parallèle (pour les grandes collections)
List<String> resultatsParalleles = services.parallelStream()
    .map(s -> s.getNom().toUpperCase())
    .collect(Collectors.toList());

// 8. Chaînage d'opérations
List<String> resultatsComplexes = services.stream()
    .filter(s -> s.getType() != null)
    .filter(s -> s.getNom().length() > 5)
    .map(Service::getNom)
    .map(String::toUpperCase)
    .distinct()
    .sorted()
    .limit(10)
    .collect(Collectors.toList());
```

**Avantages :**
- Code plus lisible et déclaratif
- Optimisation automatique
- Opérations parallèles faciles
- Code plus concis

---

## 5. JavaFX (Interface Graphique)

**Concept :** JavaFX est un framework pour créer des interfaces graphiques utilisateur (GUI) en Java.

**Code exemple dans le projet :**

```java
// Fichier: DashboardApplication.java

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class DashboardApplication extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Dashboard Services Publics");
        
        // 1. Créer un layout principal (BorderPane)
        BorderPane rootLayout = new BorderPane();
        
        // 2. Créer un header (HBox)
        HBox header = new HBox();
        header.setStyle("-fx-background-color: #2c3e50;");
        Label title = new Label("🏛️ Dashboard Services Publics");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");
        header.getChildren().add(title);
        
        // 3. Créer un menu latéral (VBox)
        VBox menuBar = new VBox(10);
        menuBar.setStyle("-fx-background-color: #34495e; -fx-min-width: 250px;");
        
        Button btnDashboard = new Button("📊 Dashboard");
        Button btnServices = new Button("🔧 Services");
        Button btnPersonnel = new Button("👥 Personnel");
        
        // 4. Ajouter des actions aux boutons
        btnDashboard.setOnAction(e -> {
            System.out.println("Dashboard cliqué");
            // Afficher le dashboard
        });
        
        btnServices.setOnAction(e -> {
            System.out.println("Services cliqué");
            // Afficher les services
        });
        
        menuBar.getChildren().addAll(btnDashboard, btnServices, btnPersonnel);
        
        // 5. Créer une zone de contenu
        StackPane contentArea = new StackPane();
        Label contentLabel = new Label("Bienvenue sur le Dashboard");
        contentArea.getChildren().add(contentLabel);
        
        // 6. Assembler le layout
        rootLayout.setTop(header);
        rootLayout.setLeft(menuBar);
        rootLayout.setCenter(contentArea);
        
        // 7. Créer la scène et afficher
        Scene scene = new Scene(rootLayout, 1400, 900);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
```

**Composants JavaFX principaux :**

```java
// Labels et textes
Label label = new Label("Texte");
label.setStyle("-fx-font-size: 14px; -fx-text-fill: blue;");

// Boutons
Button button = new Button("Cliquez-moi");
button.setOnAction(e -> System.out.println("Bouton cliqué"));

// Champs de texte
TextField textField = new TextField();
String valeur = textField.getText();

// Tableaux (TableView)
TableView<Service> table = new TableView<>();
TableColumn<Service, String> nomCol = new TableColumn<>("Nom");
nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
table.getColumns().add(nomCol);

// Layouts
VBox vbox = new VBox(10);  // Vertical, espacement 10
HBox hbox = new HBox(10);  // Horizontal, espacement 10
BorderPane borderPane = new BorderPane();  // Top, Bottom, Left, Right, Center
GridPane gridPane = new GridPane();  // Grille
```

**Lancement de l'application JavaFX :**

```batch
# Avec run-javafx.bat
run-javafx.bat
```

---

## Comparaison des Concepts

| Concept | Type | Usage Principal | Complexité |
|---------|------|-----------------|------------|
| **JDBC** | API Java | Accès direct à la base de données | Moyenne |
| **Hibernate** | Framework ORM | Mapping objet-relationnel | Élevée |
| **Thread** | Programmation concurrente | Tâches parallèles | Élevée |
| **Stream** | API Java 8+ | Traitement de collections | Moyenne |
| **JavaFX** | Framework GUI | Interfaces graphiques | Moyenne-Élevée |

---

## Exemples Complets par Concept

### Exemple JDBC Complet (ServiceDAO.java)

```java
public class ServiceDAO {
    private DatabaseManager dbManager;
    
    public ServiceDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    public void insert(Service service) {
        String sql = "INSERT INTO service (type, nom, description) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, service.getType());
            pstmt.setString(2, service.getNom());
            pstmt.setString(3, service.getDescription());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }
    
    public List<Service> findAll() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM service ORDER BY nom";
        try (Statement stmt = dbManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Service service = new Service();
                service.setId(rs.getLong("id"));
                service.setType(rs.getString("type"));
                service.setNom(rs.getString("nom"));
                service.setDescription(rs.getString("description"));
                services.add(service);
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        return services;
    }
}
```

### Exemple Hibernate Complet (ServiceDAOHibernate.java)

```java
public class ServiceDAOHibernate {
    private SessionFactory sessionFactory;
    
    public ServiceDAOHibernate() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }
    
    public void insert(Service service) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(service);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    public List<Service> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Service", Service.class).getResultList();
        }
    }
}
```

### Exemple Thread Complet (AsyncDatabaseService.java)

```java
public class AsyncDatabaseService {
    private ThreadPoolManager threadPool;
    
    public CompletableFuture<List<Service>> findAllServicesAsync() {
        return threadPool.executeAsync(() -> {
            // Cette tâche s'exécute dans un thread séparé
            return serviceDAO.findAll();
        });
    }
    
    // Utilisation
    findAllServicesAsync().thenAccept(services -> {
        System.out.println("Services reçus: " + services.size());
        // Mettre à jour l'interface utilisateur
    });
}
```

### Exemple Stream Complet

```java
// Analyser les consommations
List<Consommation> consommations = consommationDAO.findAll();

// Statistiques avec Streams
double moyenne = consommations.stream()
    .mapToDouble(Consommation::getValeur)
    .average()
    .orElse(0.0);

double max = consommations.stream()
    .mapToDouble(Consommation::getValeur)
    .max()
    .orElse(0.0);

// Regrouper par zone
Map<String, List<Consommation>> parZone = consommations.stream()
    .collect(Collectors.groupingBy(Consommation::getZone));

// Filtrer et transformer
List<String> zonesActives = consommations.stream()
    .filter(c -> c.getValeur() > 100)
    .map(Consommation::getZone)
    .distinct()
    .sorted()
    .collect(Collectors.toList());
```

---

## Quand Utiliser Chaque Concept ?

- **JDBC** : Quand vous avez besoin de contrôle total sur les requêtes SQL, performances critiques
- **Hibernate** : Quand vous voulez moins de code, développement rapide, mapping automatique
- **Thread** : Quand vous avez des tâches longues, traitement parallèle, opérations asynchrones
- **Stream** : Quand vous travaillez avec des collections, besoin de filtrer/trier/transformer
- **JavaFX** : Quand vous créez une interface graphique desktop, applications riches

---

## Fichiers de Référence dans le Projet

- **JDBC** : `dao/ServiceDAO.java`, `DatabaseManager.java`
- **Hibernate** : `dao/ServiceDAOHibernate.java`, `util/HibernateUtil.java`, `resources/hibernate.cfg.xml`
- **Thread** : `util/ThreadPoolManager.java`, `service/AsyncDatabaseService.java`
- **Stream** : `util/StreamUtils.java`, `exemple/ExempleStreamsThreads.java`
- **JavaFX** : `DashboardApplication.java`, `ui/*.java`

---

**Note :** Ce projet utilise actuellement JDBC par défaut. Hibernate est disponible mais optionnel. Les threads et streams sont utilisés pour certaines fonctionnalités avancées. JavaFX est disponible pour l'interface graphique.


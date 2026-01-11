# Guide d'intégration Hibernate

## 📋 Résumé

Hibernate a été intégré dans le projet comme alternative à JDBC natif. Vous pouvez maintenant utiliser Hibernate pour l'accès aux données.

## ✅ Fichiers créés

### Configuration
- **`resources/hibernate.cfg.xml`** - Fichier de configuration Hibernate
- **`util/HibernateUtil.java`** - Classe utilitaire pour gérer SessionFactory

### Entités avec annotations JPA
- **`Service.java`** - Ajout des annotations `@Entity`, `@Table`, `@Id`, `@Column`
- **`Admin.java`** - Ajout des annotations JPA/Hibernate

### DAO exemple
- **`dao/ServiceDAOHibernate.java`** - Exemple de DAO utilisant Hibernate

### Scripts d'installation
- **`INSTALLER_HIBERNATE.bat`** - Script pour télécharger les dépendances
- **`telecharger-hibernate.ps1`** - Script PowerShell de téléchargement

## 🚀 Installation

### Étape 1 : Télécharger les dépendances Hibernate

Exécutez le script d'installation :

```batch
INSTALLER_HIBERNATE.bat
```

Ou téléchargez manuellement depuis Maven Central :
- **hibernate-core-6.4.4.jar** : https://repo1.maven.org/maven2/org/hibernate/orm/hibernate-core/6.4.4/
- **jakarta.persistence-api-3.1.0.jar** : https://repo1.maven.org/maven2/jakarta/persistence/jakarta.persistence-api/3.1.0/
- **jboss-logging-3.5.3.Final.jar** : https://repo1.maven.org/maven2/org/jboss/logging/jboss-logging/3.5.3.Final/

Placez les fichiers JAR dans le dossier `lib/`

### Étape 2 : Mettre à jour le classpath

Lors de la compilation, ajoutez les JAR Hibernate au classpath :

```batch
javac -cp "lib\*.jar;com\municipal\dashboard\lib\mysql-connector-j-9.5.0.jar" ...
```

## 📝 Utilisation

### Utiliser HibernateUtil

```java
import com.municipal.dashboard.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

// Obtenir la SessionFactory
SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

// Ouvrir une session
Session session = sessionFactory.openSession();

// Utiliser la session...
// session.get(Service.class, 1L);
// session.persist(service);
// etc.

// Fermer la session
session.close();
```

### Exemple avec ServiceDAOHibernate

```java
import com.municipal.dashboard.dao.ServiceDAOHibernate;
import com.municipal.dashboard.Service;

ServiceDAOHibernate dao = new ServiceDAOHibernate();

// Insérer
Service service = new Service("Type", "Nom", "Description");
dao.insert(service);

// Trouver tous
List<Service> services = dao.findAll();

// Trouver par ID
Service s = dao.findById(1L);

// Mettre à jour
service.setNom("Nouveau nom");
dao.update(service);

// Supprimer
dao.delete(1L);
```

## 🔄 Migration depuis JDBC vers Hibernate

### Ancien code (JDBC)
```java
String sql = "INSERT INTO service (type, nom, description) VALUES (?, ?, ?)";
PreparedStatement pstmt = connection.prepareStatement(sql);
pstmt.setString(1, service.getType());
pstmt.setString(2, service.getNom());
pstmt.setString(3, service.getDescription());
pstmt.executeUpdate();
```

### Nouveau code (Hibernate)
```java
Transaction transaction = session.beginTransaction();
session.persist(service);
transaction.commit();
```

## ⚙️ Configuration

Le fichier `resources/hibernate.cfg.xml` contient toute la configuration :

- **Base de données** : MySQL (localhost:3306/services_publics)
- **Dialecte** : MySQL8Dialect
- **Pool de connexions** : 10 connexions
- **Affichage SQL** : Activé (utile pour le débogage)
- **Gestion du schéma** : `update` (met à jour automatiquement)

### Modifier la configuration

Éditez `resources/hibernate.cfg.xml` pour changer :
- L'URL de connexion
- Les identifiants
- Le dialecte
- Le comportement du schéma

## 📚 Annotations JPA/Hibernate

### Exemple d'entité

```java
@Entity
@Table(name = "service")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "nom", nullable = false, length = 100)
    private String nom;
    
    // Getters et setters...
}
```

### Annotations principales

- `@Entity` - Marque la classe comme entité
- `@Table(name = "nom_table")` - Nom de la table en BDD
- `@Id` - Clé primaire
- `@GeneratedValue` - Auto-génération de l'ID
- `@Column` - Mapping d'une colonne
- `@ManyToOne`, `@OneToMany`, etc. - Relations

## ⚠️ Notes importantes

1. **Hibernate et JDBC coexistent** : Vous pouvez utiliser les deux dans le même projet
2. **Fermeture de SessionFactory** : Appelez `HibernateUtil.shutdown()` à la fermeture de l'application
3. **Transactions** : N'oubliez pas de gérer les transactions avec `beginTransaction()` et `commit()`
4. **Sessions** : Fermez toujours les sessions après utilisation (try-with-resources recommandé)

## 🔍 Différences JDBC vs Hibernate

| Aspect | JDBC | Hibernate |
|--------|------|-----------|
| Requêtes | SQL écrites manuellement | HQL/JPQL ou méthodes |
| Mapping | Manuel (ResultSet) | Automatique (annotations) |
| Performance | Optimisation manuelle | Cache intégré |
| Complexité | Plus de code | Moins de code |
| Flexibilité | Maximum | Bonne |

## 📖 Ressources

- Documentation Hibernate : https://hibernate.org/orm/documentation/
- Documentation JPA : https://jakarta.ee/specifications/persistence/
- Guide HQL : https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#hql

## 🎯 Prochaines étapes

1. Télécharger les dépendances avec `INSTALLER_HIBERNATE.bat`
2. Ajouter les JAR au classpath lors de la compilation
3. Tester avec `ServiceDAOHibernate`
4. Migrer progressivement les autres DAO
5. Utiliser les relations (@ManyToOne, @OneToMany, etc.)


# Configuration XAMPP MySQL

Ce projet utilise **XAMPP MySQL** pour la base de données.

## 📋 Configuration

### Identifiants par défaut XAMPP
- **Host:** localhost
- **Port:** 3306
- **Database:** services_publics
- **User:** root
- **Password:** (vide)

## 🚀 Démarrage

### 1. Démarrer XAMPP
1. Ouvrez le panneau de contrôle XAMPP
2. Démarrez le service **MySQL**
3. Vérifiez que MySQL est actif (icône verte)

### 2. Créer la base de données
La base de données `services_publics` sera créée automatiquement au premier lancement de l'application.

Ou manuellement via phpMyAdmin:
1. Ouvrez http://localhost/phpmyadmin
2. Créez une nouvelle base de données: `services_publics`
3. Charset: `utf8mb4_unicode_ci`

### 3. Lancer l'application
```bash
# Compilation
javac -encoding UTF-8 -cp "lib/mysql-connector-java.jar" *.java

# Exécution
java -cp ".;lib/mysql-connector-java.jar" com.municipal.dashboard.MainApp
```

## 🔧 Modification des identifiants

Si vous avez modifié les identifiants MySQL dans XAMPP, modifiez les valeurs dans:
- `DatabaseManager.java`
- `Database.java`

## ✅ Vérification

Pour vérifier que MySQL fonctionne:
1. Ouvrez phpMyAdmin: http://localhost/phpmyadmin
2. Connectez-vous avec `root` (sans mot de passe)
3. Vérifiez que la base `services_publics` existe

## 📝 Notes

- XAMPP MySQL utilise par défaut le port 3306
- L'utilisateur `root` n'a pas de mot de passe par défaut
- Toutes les tables sont créées automatiquement au premier lancement


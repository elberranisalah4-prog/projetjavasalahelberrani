# Guide de Sécurisation MySQL/phpMyAdmin

## 📋 Vue d'ensemble

Ce guide explique comment sécuriser votre installation MySQL/phpMyAdmin avec XAMPP pour le projet Dashboard Services Publics.

## 🔧 Configuration actuelle

- **Base de données**: `services_publics`
- **Hôte**: `localhost`
- **Port**: `3306`
- **Utilisateur par défaut**: `root` (sans mot de passe)

## 🔒 Étapes de sécurisation

### 1. Exécuter le script de sécurisation

**Option A - Via le script batch:**
```batch
SECURISER_MYSQL.bat
```

**Option B - Via phpMyAdmin:**
1. Ouvrez phpMyAdmin: http://localhost/phpmyadmin
2. Cliquez sur l'onglet "SQL"
3. Ouvrez le fichier `secure_mysql.sql`
4. Copiez-collez son contenu dans la zone SQL
5. Cliquez sur "Exécuter"

**Option C - Via ligne de commande MySQL:**
```bash
cd C:\xampp\mysql\bin
mysql.exe -u root < C:\src\main\java\com\municipal\dashboard\secure_mysql.sql
```

### 2. Ce que fait le script de sécurisation

- ✅ Crée un utilisateur dédié `dashboard_user` avec un mot de passe fort
- ✅ Accorde uniquement les privilèges nécessaires sur `services_publics`
- ✅ Supprime les utilisateurs anonymes
- ✅ Supprime les bases de données de test

### 3. Configurer l'application pour utiliser l'utilisateur sécurisé

Après avoir exécuté le script, modifiez le fichier `db_config.properties` (créé automatiquement) :

```properties
db.host=localhost
db.port=3306
db.name=services_publics
db.user=dashboard_user
db.password=Dashboard2024!Secure
db.useSSL=false
db.timezone=UTC
```

**⚠️ IMPORTANT**: Changez le mot de passe par défaut `Dashboard2024!Secure` par un mot de passe fort de votre choix!

### 4. Sécuriser phpMyAdmin

#### A. Modifier config.inc.php

Éditez le fichier: `C:\xampp\phpMyAdmin\config.inc.php`

Ajoutez/modifiez ces lignes:
```php
$cfg['Servers'][$i]['auth_type'] = 'cookie';
$cfg['Servers'][$i]['AllowNoPassword'] = false;
$cfg['Servers'][$i]['host'] = '127.0.0.1';
```

#### B. Protéger phpMyAdmin avec .htaccess

1. Créez le dossier `C:\xampp\security\` s'il n'existe pas

2. Créez le fichier `C:\xampp\phpMyAdmin\.htaccess`:
```apache
AuthType Basic
AuthName "Accès phpMyAdmin - Dashboard Services Publics"
AuthUserFile C:/xampp/security/.htpasswd
Require valid-user
```

3. Créez le fichier de mots de passe:
```bash
cd C:\xampp\apache\bin
htpasswd -c C:\xampp\security\.htpasswd admin
```
(Entrez un mot de passe fort quand demandé)

### 5. Sécuriser le compte root MySQL (optionnel mais recommandé)

Dans phpMyAdmin ou via MySQL:
```sql
ALTER USER 'root'@'localhost' IDENTIFIED BY 'VotreMotDePasseRootFort123!';
FLUSH PRIVILEGES;
```

## 📝 Utilisation de DatabaseConfig

La classe `DatabaseConfig` permet de gérer la configuration de manière sécurisée:

```java
// Lire la configuration
String host = DatabaseConfig.getHost();
String user = DatabaseConfig.getUser();
String password = DatabaseConfig.getPassword();

// Modifier la configuration
DatabaseConfig.setUser("dashboard_user");
DatabaseConfig.setPassword("NouveauMotDePasse123!");
DatabaseConfig.setUseSSL(true); // Pour activer SSL
```

## ✅ Vérification

Pour vérifier que tout fonctionne:

1. **Test de connexion:**
   ```java
   DatabaseManager dbManager = DatabaseManager.getInstance();
   Connection conn = dbManager.getConnection();
   if (conn != null) {
       System.out.println("✓ Connexion réussie!");
   }
   ```

2. **Vérifier les utilisateurs MySQL:**
   ```sql
   SELECT User, Host FROM mysql.user;
   ```

3. **Tester l'accès phpMyAdmin:**
   - Ouvrez http://localhost/phpmyadmin
   - Vous devriez être invité à entrer le mot de passe .htaccess
   - Connectez-vous avec les identifiants MySQL

## 🚨 Bonnes pratiques de sécurité

1. ✅ **Utilisez toujours un mot de passe fort** (minimum 12 caractères, majuscules, minuscules, chiffres, symboles)
2. ✅ **Ne partagez jamais les mots de passe** en clair
3. ✅ **Utilisez un utilisateur dédié** au lieu de root pour l'application
4. ✅ **Limitez les privilèges** aux besoins réels
5. ✅ **Activez SSL** en production
6. ✅ **Faites des sauvegardes régulières** de la base de données
7. ✅ **Mettez à jour régulièrement** MySQL et phpMyAdmin

## 🔐 En cas de problème

Si vous oubliez le mot de passe root:
1. Arrêtez MySQL dans XAMPP
2. Démarrez MySQL en mode sécurisé:
   ```bash
   C:\xampp\mysql\bin\mysqld.exe --skip-grant-tables
   ```
3. Connectez-vous sans mot de passe et réinitialisez:
   ```sql
   ALTER USER 'root'@'localhost' IDENTIFIED BY 'NouveauMotDePasse';
   FLUSH PRIVILEGES;
   ```

## 📞 Support

Pour toute question ou problème, consultez:
- La documentation MySQL: https://dev.mysql.com/doc/
- La documentation phpMyAdmin: https://www.phpmyadmin.net/docs/



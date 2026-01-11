# Guide de Résolution - MySQL XAMPP qui s'arrête

## 🔴 Erreur: "MySQL shutdown unexpectedly"

Cette erreur est courante dans XAMPP. Voici les solutions par ordre de priorité.

## ✅ Solution 1: Vérifier le port 3306 (le plus fréquent)

### Problème
Un autre service utilise déjà le port 3306 (Skype, autre instance MySQL, etc.)

### Solution rapide
1. Ouvrez le **Gestionnaire des tâches** (Ctrl+Shift+Esc)
2. Onglet **Détails**
3. Cherchez les processus:
   - `mysqld.exe`
   - `mysql.exe`
   - `skype.exe` (peut utiliser le port 3306)
4. Terminez ces processus
5. Redémarrez MySQL dans XAMPP

### Solution via script
```batch
FIX_MYSQL_XAMPP.bat
```
Choisissez l'option 1 pour redémarrer MySQL.

### Solution manuelle
```batch
REM Voir qui utilise le port 3306
netstat -ano | findstr ":3306"

REM Arrêter le processus (remplacez PID par le numéro trouvé)
taskkill /F /PID [PID]
```

## ✅ Solution 2: Vérifier les logs MySQL

1. Ouvrez le **Panneau de contrôle XAMPP**
2. Cliquez sur **Logs** à côté de MySQL
3. Ou consultez: `C:\xampp\mysql\data\*.err`
4. Cherchez les erreurs récentes

### Erreurs courantes et solutions

**"Can't create/write to file"**
- Problème de permissions
- Solution: Exécutez XAMPP en tant qu'administrateur

**"Table 'mysql.user' doesn't exist"**
- Base de données système corrompue
- Solution: Réinitialiser MySQL (voir Solution 3)

**"Access denied"**
- Problème d'authentification
- Solution: Réinitialiser le mot de passe root

## ✅ Solution 3: Réinitialiser MySQL (si les données ne sont pas importantes)

⚠️ **ATTENTION**: Cela supprime TOUTES les bases de données!

### Via script
```batch
FIX_MYSQL_XAMPP.bat
```
Choisissez l'option 2.

### Manuellement
1. Arrêtez MySQL dans XAMPP
2. Sauvegardez `C:\xampp\mysql\bin\my.ini` (optionnel)
3. Supprimez le dossier `C:\xampp\mysql\data`
4. Créez un nouveau dossier `data`
5. Ouvrez PowerShell en tant qu'administrateur:
   ```powershell
   cd C:\xampp\mysql\bin
   .\mysqld.exe --initialize-insecure --datadir=C:\xampp\mysql\data
   ```
6. Redémarrez MySQL dans XAMPP

## ✅ Solution 4: Vérifier la configuration my.ini

1. Ouvrez `C:\xampp\mysql\bin\my.ini`
2. Vérifiez ces paramètres:

```ini
[mysqld]
port=3306
datadir=C:/xampp/mysql/data
```

3. Si le fichier est corrompu, restaurez depuis une sauvegarde ou réinstallez XAMPP

## ✅ Solution 5: Permissions Windows

1. Clic droit sur `C:\xampp\mysql\data`
2. **Propriétés** → **Sécurité**
3. Assurez-vous que votre utilisateur a les droits:
   - **Lecture et exécution**
   - **Écriture**
   - **Contrôle total** (recommandé pour développement)

## ✅ Solution 6: Réinstaller XAMPP

Si rien ne fonctionne:

1. **Sauvegardez vos bases de données** (exportez via phpMyAdmin si accessible)
2. Désinstallez XAMPP
3. Supprimez le dossier `C:\xampp` s'il reste
4. Téléchargez XAMPP depuis https://www.apachefriends.org/
5. Réinstallez XAMPP
6. Restaurez vos bases de données

## 🔧 Solutions avancées

### Changer le port MySQL

Si le port 3306 est bloqué de manière permanente:

1. Éditez `C:\xampp\mysql\bin\my.ini`:
   ```ini
   [mysqld]
   port=3307
   ```
2. Redémarrez MySQL
3. Mettez à jour votre application:
   ```java
   // Dans DatabaseConfig.java ou DatabaseManager.java
   private static final String DB_PORT = "3307";
   ```

### Démarrer MySQL en mode debug

Pour voir les erreurs en temps réel:

```batch
cd C:\xampp\mysql\bin
mysqld.exe --console
```

## 📋 Checklist de diagnostic

- [ ] Port 3306 libre?
- [ ] MySQL démarré en tant qu'administrateur?
- [ ] Dossier `C:\xampp\mysql\data` existe?
- [ ] Permissions correctes sur le dossier data?
- [ ] Fichier `my.ini` valide?
- [ ] Pas de conflit avec un autre MySQL?
- [ ] Logs MySQL consultés?
- [ ] Antivirus ne bloque pas MySQL?

## 🆘 Si rien ne fonctionne

1. **Consultez les logs Windows**:
   - Ouvrez **Observateur d'événements**
   - **Journaux Windows** → **Application**
   - Cherchez les erreurs MySQL

2. **Forum XAMPP**: https://community.apachefriends.org/

3. **Vérifiez la version de Windows**: Certaines versions ont des problèmes connus

## 💡 Prévention

Pour éviter ce problème à l'avenir:

1. ✅ Ne fermez jamais XAMPP brutalement (utilisez "Stop")
2. ✅ Faites des sauvegardes régulières de vos bases
3. ✅ Évitez d'avoir plusieurs instances MySQL
4. ✅ Utilisez un antivirus qui n'interfère pas avec MySQL
5. ✅ Gardez XAMPP à jour

## 🔗 Liens utiles

- Documentation XAMPP: https://www.apachefriends.org/docs/
- Documentation MySQL: https://dev.mysql.com/doc/
- Forum XAMPP: https://community.apachefriends.org/



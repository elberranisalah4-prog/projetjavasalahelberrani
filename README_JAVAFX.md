# Guide d'installation et d'utilisation de JavaFX

## 📋 Prérequis
- Java 17 ou supérieur (vous avez déjà Java 17 ✅)
- Télécharger JavaFX SDK depuis https://openjfx.io/

## 🚀 Installation rapide

### Étape 1 : Télécharger JavaFX SDK

1. Allez sur https://openjfx.io/
2. Cliquez sur "Download"
3. Choisissez :
   - **Version** : 17 (ou celle correspondant à votre Java)
   - **Platform** : Windows
   - **Type** : SDK
4. Téléchargez le fichier ZIP (ex: `openjfx-17.0.x_windows-x64_bin-sdk.zip`)

### Étape 2 : Extraire JavaFX

1. Trouvez le fichier ZIP téléchargé
2. Faites un clic droit → **Extraire tout...**
3. Extrayez dans un dossier facile à retenir, par exemple :
   ```
   C:\javafx-sdk-17.0.2
   ```

### Étape 3 : Configurer JavaFX dans le projet

1. Exécutez le script `configurerJavaFX.bat`
2. Quand il demande le chemin, entrez le chemin vers le dossier **lib** :
   ```
   C:\javafx-sdk-17.0.2\lib
   ```
3. Appuyez sur Entrée

✅ La configuration est sauvegardée dans `javafx_path.txt`

### Étape 4 : Lancer l'application JavaFX

Exécutez simplement :
```batch
run-javafx.bat
```

L'application va :
1. Compiler tous les fichiers (y compris les fichiers JavaFX)
2. Lancer l'interface graphique JavaFX

## 📝 Scripts disponibles

### `run-javafx.bat`
Lance l'application avec l'interface graphique JavaFX.

### `run.bat`
Lance l'application en mode console (sans JavaFX).

### `configurerJavaFX.bat`
Configure le chemin vers JavaFX SDK.

## ⚠️ Dépannage

### Erreur : "JavaFX n'est pas configuré"
- Exécutez `configurerJavaFX.bat` et entrez le chemin correct

### Erreur : "javafx.controls.jar non trouvé"
- Vérifiez que le chemin pointe vers le dossier **lib** (pas le dossier SDK)
- Exemple correct : `C:\javafx-sdk-17.0.2\lib`
- Exemple incorrect : `C:\javafx-sdk-17.0.2`

### Erreur de compilation JavaFX
- Vérifiez que vous avez bien téléchargé la version 17 de JavaFX
- Vérifiez que le chemin dans `javafx_path.txt` est correct

### L'application ne démarre pas
- Vérifiez que MySQL est démarré (si nécessaire)
- Vérifiez que le driver MySQL est présent dans `com\municipal\dashboard\lib\`

## 📁 Structure des fichiers

```
projet/
├── run.bat              → Lance l'app console
├── run-javafx.bat       → Lance l'app JavaFX ⭐
├── configurerJavaFX.bat → Configure JavaFX
├── javafx_path.txt      → Chemin vers JavaFX (créé automatiquement)
└── GUIDE_RAPIDE_JAVAFX.txt → Guide détaillé
```

## 💡 Astuces

- Vous pouvez avoir les deux versions : console (`run.bat`) et graphique (`run-javafx.bat`)
- Le fichier `javafx_path.txt` contient le chemin vers JavaFX - vous pouvez le modifier manuellement si nécessaire
- Si vous changez l'emplacement de JavaFX, réexécutez `configurerJavaFX.bat`


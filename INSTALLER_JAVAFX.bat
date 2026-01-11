@echo off
echo ========================================
echo   INSTALLATION JAVAFX - MENU
echo ========================================
echo.
echo Choisissez une option:
echo.
echo 1. Telecharger et installer JavaFX automatiquement (RECOMMANDE)
echo 2. Configurer le chemin manuellement (si vous avez deja JavaFX)
echo 3. Voir les instructions de telechargement manuel
echo 0. Quitter
echo.
set /p CHOIX="Votre choix (0-3): "

if "%CHOIX%"=="1" (
    echo.
    echo Lancement du telechargement automatique...
    echo.
    call telecharger-javafx.bat
) else if "%CHOIX%"=="2" (
    echo.
    echo Configuration manuelle...
    echo.
    call configurerJavaFX.bat
) else if "%CHOIX%"=="3" (
    echo.
    echo ========================================
    echo   INSTRUCTIONS DE TELECHARGEMENT MANUEL
    echo ========================================
    echo.
    echo 1. Ouvrez votre navigateur
    echo 2. Allez sur: https://openjfx.io/
    echo 3. Cliquez sur "Download"
    echo 4. Choisissez:
    echo    - Version: 17 (ou celle correspondant a votre Java)
    echo    - Platform: Windows
    echo    - Type: SDK
    echo 5. Telechargez le fichier ZIP
    echo 6. Extrayez-le dans un dossier (ex: C:\javafx-sdk-17.0.2)
    echo 7. Retournez au menu et choisissez l'option 2
    echo.
    pause
    goto :eof
) else if "%CHOIX%"=="0" (
    exit /b 0
) else (
    echo Choix invalide!
    pause
    goto :eof
)

pause


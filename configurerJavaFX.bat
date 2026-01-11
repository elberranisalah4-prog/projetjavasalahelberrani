@echo off
echo ========================================
echo   CONFIGURATION JAVAFX
echo ========================================
echo.

REM Aller au répertoire du script
cd /d "%~dp0"

echo Instructions:
echo 1. Telechargez JavaFX SDK depuis https://openjfx.io/
echo 2. Choisissez la version 17 (ou celle correspondant a votre Java)
echo 3. Choisissez Windows x64
echo 4. Extrayez le ZIP dans un dossier (ex: C:\javafx-sdk-17.0.2)
echo 5. Le chemin doit pointer vers le dossier "lib" a l'interieur
echo.
echo Exemple de chemin correct:
echo   C:\javafx-sdk-17.0.2\lib
echo.
echo ========================================
echo.

set /p JAVAFX_PATH="Entrez le chemin vers le dossier lib de JavaFX: "

REM Vérifier que le chemin existe
if not exist "%JAVAFX_PATH%" (
    echo.
    echo ERREUR: Le chemin n'existe pas: %JAVAFX_PATH%
    pause
    exit /b 1
)

REM Vérifier que c'est bien le dossier lib
if not exist "%JAVAFX_PATH%\javafx.controls.jar" (
    echo.
    echo ATTENTION: javafx.controls.jar non trouve dans ce dossier.
    echo Assurez-vous que le chemin pointe vers le dossier "lib" de JavaFX.
    echo.
    set /p CONTINUE="Voulez-vous continuer quand meme? (O/N): "
    if /i not "%CONTINUE%"=="O" (
        exit /b 1
    )
)

REM Sauvegarder le chemin dans un fichier
echo %JAVAFX_PATH% > javafx_path.txt

echo.
echo ========================================
echo Configuration sauvegardee!
echo.
echo Chemin JavaFX: %JAVAFX_PATH%
echo Fichier de configuration: javafx_path.txt
echo.
echo Vous pouvez maintenant lancer l'application avec "run-javafx.bat"
echo ========================================
echo.
pause


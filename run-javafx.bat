@echo off
echo ========================================
echo   DASHBOARD SERVICES PUBLICS
echo   Lancement de l'application JavaFX...
echo ========================================
echo.

REM Aller au répertoire racine du projet (C:\src\main\java)
cd /d "%~dp0"
cd ..\..\..

REM Activer la variable d'environnement pour les boucles
setlocal enabledelayedexpansion

REM Vérifier si JavaFX est configuré
if not exist "javafx_path.txt" (
    echo ERREUR: JavaFX n'est pas configure!
    echo.
    echo ========================================
    echo   OPTIONS D'INSTALLATION
    echo ========================================
    echo.
    echo OPTION 1 (RECOMMANDE): Installation automatique
    echo   Executez: INSTALLER_JAVAFX.bat
    echo   Puis choisissez l'option 1 pour telecharger automatiquement
    echo.
    echo OPTION 2: Installation manuelle
    echo   1. Telechargez JavaFX SDK depuis https://openjfx.io/
    echo   2. Extrayez-le dans un dossier (ex: C:\javafx-sdk-17.0.2)
    echo   3. Executez: INSTALLER_JAVAFX.bat et choisissez l'option 2
    echo      Ou executez: configurerJavaFX.bat
    echo      Entrez le chemin vers le dossier lib
    echo      Exemple: C:\javafx-sdk-17.0.2\lib
    echo.
    echo ========================================
    echo.
    pause
    exit /b 1
)

REM Lire le chemin JavaFX depuis le fichier
set /p JAVAFX_PATH=<javafx_path.txt
REM Supprimer les espaces en fin de ligne
set JAVAFX_PATH=!JAVAFX_PATH: =!

REM Vérifier que le chemin existe
if not exist "%JAVAFX_PATH%" (
    echo ERREUR: Le chemin JavaFX n'existe pas: %JAVAFX_PATH%
    echo Veuillez reconfigurer JavaFX avec "configurerJavaFX.bat"
    pause
    exit /b 1
)

REM Vérifier que les fichiers JAR JavaFX existent
if not exist "%JAVAFX_PATH%\javafx.controls.jar" (
    echo ERREUR: javafx.controls.jar non trouve dans %JAVAFX_PATH%
    echo Veuillez verifier que le chemin pointe vers le dossier lib de JavaFX
    pause
    exit /b 1
)

if not exist "com\municipal\dashboard\lib\mysql-connector-j-9.5.0.jar" (
    echo ERREUR: Le driver MySQL n'est pas trouve dans le dossier com\municipal\dashboard\lib\
    echo Veuillez verifier que mysql-connector-j-9.5.0.jar est present
    pause
    exit /b 1
)

echo Compilation en cours (avec JavaFX)...
echo Chemin JavaFX: !JAVAFX_PATH!
echo.

REM Construire le classpath avec tous les JAR JavaFX et MySQL
set CLASSPATH=com\municipal\dashboard\lib\mysql-connector-j-9.5.0.jar;!JAVAFX_PATH!\javafx.base.jar;!JAVAFX_PATH!\javafx.controls.jar;!JAVAFX_PATH!\javafx.fxml.jar;!JAVAFX_PATH!\javafx.graphics.jar;!JAVAFX_PATH!\javafx.swing.jar;!JAVAFX_PATH!\javafx.media.jar;!JAVAFX_PATH!\javafx.web.jar

REM Compiler avec JavaFX
javac -encoding UTF-8 -cp "!CLASSPATH!" ^
com\municipal\dashboard\*.java ^
com\municipal\dashboard\dao\*.java ^
com\municipal\dashboard\ui\*.java ^
com\municipal\dashboard\service\*.java ^
com\municipal\dashboard\util\*.java ^
com\municipal\dashboard\repository\*.java ^
com\municipal\dashboard\model\*.java ^
com\municipal\dashboard\exception\*.java

if errorlevel 1 (
    echo ERREUR lors de la compilation!
    pause
    exit /b 1
)

echo.
echo Lancement de l'application JavaFX...
echo.

REM Construire le module path pour JavaFX (Java 9+)
REM Le module path pointe vers le dossier lib qui contient les modules JavaFX
set JAVA_OPTS=--module-path "!JAVAFX_PATH!" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base

REM Exécuter l'application JavaFX
REM Note: Application.launch() est appelé automatiquement par JavaFX
echo Commande executee:
echo java -cp ".;!CLASSPATH!" !JAVA_OPTS! com.municipal.dashboard.DashboardApplication
echo.
java -cp ".;!CLASSPATH!" !JAVA_OPTS! com.municipal.dashboard.DashboardApplication

if errorlevel 1 (
    echo.
    echo ERREUR lors du lancement de l'application!
    echo Verifiez que JavaFX est correctement configure.
    pause
    exit /b 1
)

pause


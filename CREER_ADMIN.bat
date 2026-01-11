@echo off
chcp 65001 >nul
echo ========================================
echo   CRÉATION D'ADMINISTRATEUR
echo   Dashboard Services Publics
echo ========================================
echo.

REM Aller au répertoire racine du projet (C:\src\main\java)
cd /d "%~dp0"
cd ..\..\..

if not exist "com\municipal\dashboard\lib\mysql-connector-j-9.5.0.jar" (
    echo ERREUR: Le driver MySQL n'est pas trouve dans le dossier com\municipal\dashboard\lib\
    echo Veuillez verifier que mysql-connector-j-9.5.0.jar est present
    pause
    exit /b 1
)

echo Compilation en cours...
javac -encoding UTF-8 -cp "com\municipal\dashboard\lib\mysql-connector-j-9.5.0.jar" com\municipal\dashboard\*.java com\municipal\dashboard\dao\*.java com\municipal\dashboard\ui\*.java com\municipal\dashboard\service\*.java com\municipal\dashboard\util\*.java com\municipal\dashboard\repository\*.java com\municipal\dashboard\model\*.java com\municipal\dashboard\exception\*.java >nul 2>&1

if errorlevel 1 (
    echo ERREUR lors de la compilation!
    pause
    exit /b 1
)

echo.
echo Veuillez remplir les informations de l'administrateur:
echo.

set /p NOM="Nom *: "
set /p PRENOM="Prenom *: "
set /p EMAIL="Email *: "
set /p MOTDEPASSE="Mot de passe *: "
set /p TELEPHONE="Telephone (optionnel - appuyez sur Entree pour ignorer): "

echo.
echo Creation de l'administrateur en cours...
echo.

if "%TELEPHONE%"=="" (
    java -cp ".;com\municipal\dashboard\lib\mysql-connector-j-9.5.0.jar" com.municipal.dashboard.CreateAdminDirect "%NOM%" "%PRENOM%" "%EMAIL%" "%MOTDEPASSE%"
) else (
    java -cp ".;com\municipal\dashboard\lib\mysql-connector-j-9.5.0.jar" com.municipal.dashboard.CreateAdminDirect "%NOM%" "%PRENOM%" "%EMAIL%" "%MOTDEPASSE%" "%TELEPHONE%"
)

echo.
pause


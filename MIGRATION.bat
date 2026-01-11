@echo off
chcp 65001 >nul
echo ========================================
echo   MIGRATION BASE DE DONNEES MYSQL
echo   Dashboard Services Publics
echo ========================================
echo.

REM Vérifier si XAMPP est installé
if not exist "C:\xampp\mysql\bin\mysql.exe" (
    echo ERREUR: XAMPP n'est pas trouve dans C:\xampp\
    echo Veuillez installer XAMPP ou ajuster le chemin.
    pause
    exit /b 1
)

REM Vérifier si MySQL est démarré (vérifier le port 3306)
netstat -ano | findstr ":3306" >nul
if %errorlevel% neq 0 (
    echo ATTENTION: MySQL ne semble pas demarré sur le port 3306
    echo Veuillez demarrer MySQL dans XAMPP avant de continuer.
    echo.
    set /p CONTINUER="Voulez-vous continuer quand meme? (O/N): "
    if /i not "%CONTINUER%"=="O" (
        exit /b 1
    )
)

echo.
echo Options de migration disponibles:
echo.
echo   1. Migration complete (cree base + toutes les tables)
echo   2. Nettoyage et creation des tables (supprime tables Sports)
echo   3. Creation simple de la base de donnees
echo   4. Exporter la base de donnees vers SQL
echo   5. Quitter
echo.
set /p OPTION="Votre choix (1-5): "

if "%OPTION%"=="1" (
    echo.
    echo ========================================
    echo   MIGRATION COMPLETE
    echo ========================================
    echo.
    echo Execution du script: migration_complete.sql
    echo.
    
    if not exist "migration_complete.sql" (
        echo ERREUR: Le fichier migration_complete.sql n'existe pas!
        pause
        exit /b 1
    )
    
    C:\xampp\mysql\bin\mysql.exe -u root < migration_complete.sql
    
    if %errorlevel% == 0 (
        echo.
        echo Migration complete terminee avec succes!
    ) else (
        echo.
        echo ERREUR lors de la migration!
        echo Verifiez que MySQL est demarre et que vous avez les droits necessaires.
    )
    pause
    exit /b %errorlevel%
)

if "%OPTION%"=="2" (
    echo.
    echo ========================================
    echo   NETTOYAGE ET CREATION DES TABLES
    echo ========================================
    echo.
    echo ATTENTION: Ce script va supprimer les tables Sports (teams, matches, etc.)
    echo.
    set /p CONFIRM="Etes-vous sur? (O/N): "
    if /i not "%CONFIRM%"=="O" (
        echo Operation annulee.
        pause
        exit /b 0
    )
    
    echo.
    echo Execution du script: cleanup_and_create_tables.sql
    echo.
    
    if not exist "cleanup_and_create_tables.sql" (
        echo ERREUR: Le fichier cleanup_and_create_tables.sql n'existe pas!
        pause
        exit /b 1
    )
    
    C:\xampp\mysql\bin\mysql.exe -u root < cleanup_and_create_tables.sql
    
    if %errorlevel% == 0 (
        echo.
        echo Nettoyage et creation termines avec succes!
    ) else (
        echo.
        echo ERREUR lors de l'execution!
    )
    pause
    exit /b %errorlevel%
)

if "%OPTION%"=="3" (
    echo.
    echo ========================================
    echo   CREATION SIMPLE DE LA BASE
    echo ========================================
    echo.
    echo Execution du script: create_database.sql
    echo.
    
    if not exist "create_database.sql" (
        echo ERREUR: Le fichier create_database.sql n'existe pas!
        pause
        exit /b 1
    )
    
    C:\xampp\mysql\bin\mysql.exe -u root < create_database.sql
    
    if %errorlevel% == 0 (
        echo.
        echo Base de donnees creee avec succes!
    ) else (
        echo.
        echo ERREUR lors de la creation!
    )
    pause
    exit /b %errorlevel%
)

if "%OPTION%"=="4" (
    echo.
    echo ========================================
    echo   EXPORT DE LA BASE DE DONNEES
    echo ========================================
    echo.
    echo Cette option utilise le programme Java MigrateToPhpMyAdmin
    echo.
    
    REM Compiler le programme Java si nécessaire
    if not exist "MigrateToPhpMyAdmin.class" (
        echo Compilation de MigrateToPhpMyAdmin.java...
        javac -encoding UTF-8 -cp "lib\mysql-connector-j-9.5.0.jar;." MigrateToPhpMyAdmin.java DatabaseManager.java DatabaseConfig.java
        if %errorlevel% neq 0 (
            echo ERREUR lors de la compilation!
            pause
            exit /b 1
        )
    )
    
    echo Execution de l'export...
    java -cp "lib\mysql-connector-j-9.5.0.jar;." MigrateToPhpMyAdmin
    
    pause
    exit /b %errorlevel%
)

if "%OPTION%"=="5" (
    exit /b 0
)

echo.
echo Option invalide.
pause


@echo off
chcp 65001 >nul
echo ========================================
echo   DIAGNOSTIC ET REPARATION MYSQL XAMPP
echo   Dashboard Services Publics
echo ========================================
echo.

REM Vérifier si XAMPP est installé
if not exist "C:\xampp\mysql\bin\mysqld.exe" (
    echo ERREUR: XAMPP n'est pas trouve dans C:\xampp\
    echo Veuillez installer XAMPP ou ajuster le chemin.
    pause
    exit /b 1
)

echo.
echo [ETAPE 1] Verification du port 3306...
echo.

REM Vérifier si le port 3306 est utilisé
netstat -ano | findstr ":3306" >nul
if %errorlevel% == 0 (
    echo ⚠ ATTENTION: Le port 3306 est deja utilise!
    echo.
    echo Processus utilisant le port 3306:
    netstat -ano | findstr ":3306"
    echo.
    echo Voulez-vous arreter ces processus? (O/N)
    set /p CHOIX="> "
    if /i "%CHOIX%"=="O" (
        for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":3306" ^| findstr "LISTENING"') do (
            echo Arret du processus PID: %%a
            taskkill /F /PID %%a >nul 2>&1
        )
        echo ✓ Processus arretes.
        timeout /t 2 >nul
    )
) else (
    echo ✓ Port 3306 disponible
)

echo.
echo [ETAPE 2] Verification des fichiers MySQL...
echo.

REM Vérifier les fichiers de données
if not exist "C:\xampp\mysql\data" (
    echo ✗ ERREUR: Le dossier data n'existe pas!
    echo   Chemin attendu: C:\xampp\mysql\data
    pause
    exit /b 1
) else (
    echo ✓ Dossier data trouve
)

if not exist "C:\xampp\mysql\data\mysql" (
    echo ⚠ ATTENTION: Le dossier mysql dans data est manquant!
    echo   Cela peut indiquer une installation incomplete.
)

echo.
echo [ETAPE 3] Verification des logs MySQL...
echo.

if exist "C:\xampp\mysql\data\*.err" (
    echo Fichiers d'erreur trouves. Affichage des dernieres erreurs:
    echo.
    for %%f in (C:\xampp\mysql\data\*.err) do (
        echo === %%f ===
        powershell -Command "Get-Content '%%f' -Tail 20"
        echo.
    )
) else (
    echo Aucun fichier d'erreur trouve dans C:\xampp\mysql\data\
)

echo.
echo [ETAPE 4] Options de reparation...
echo.
echo Choisissez une option:
echo   1. Redemarrer MySQL (recommandé en premier)
echo   2. Reinitialiser MySQL (supprime toutes les donnees!)
echo   3. Verifier la configuration my.ini
echo   4. Reinstaller MySQL dans XAMPP
echo   5. Quitter
echo.
set /p OPTION="Votre choix (1-5): "

if "%OPTION%"=="1" (
    echo.
    echo Redemarrage de MySQL...
    echo.
    echo Arret de MySQL...
    C:\xampp\mysql_stop.bat >nul 2>&1
    timeout /t 3 >nul
    echo Demarrage de MySQL...
    C:\xampp\mysql_start.bat
    echo.
    echo ✓ MySQL redemarre. Verifiez dans le panneau de controle XAMPP.
    pause
    exit /b 0
)

if "%OPTION%"=="2" (
    echo.
    echo ⚠ ATTENTION: Cette operation va SUPPRIMER toutes les donnees MySQL!
    echo   Toutes les bases de donnees seront perdues!
    echo.
    set /p CONFIRM="Etes-vous sur? Tapez OUI pour confirmer: "
    if /i not "%CONFIRM%"=="OUI" (
        echo Operation annulee.
        pause
        exit /b 0
    )
    
    echo.
    echo Arret de MySQL...
    C:\xampp\mysql_stop.bat >nul 2>&1
    timeout /t 3 >nul
    
    echo Sauvegarde de la configuration...
    if exist "C:\xampp\mysql\bin\my.ini" (
        copy "C:\xampp\mysql\bin\my.ini" "C:\xampp\mysql\bin\my.ini.backup" >nul
    )
    
    echo Suppression des donnees...
    if exist "C:\xampp\mysql\data" (
        rd /s /q "C:\xampp\mysql\data" 2>nul
    )
    
    echo Creation du nouveau dossier data...
    mkdir "C:\xampp\mysql\data" >nul 2>&1
    
    echo Initialisation de MySQL...
    C:\xampp\mysql\bin\mysqld.exe --initialize-insecure --datadir=C:\xampp\mysql\data
    
    echo.
    echo ✓ MySQL reinitialise. Redemarrez MySQL dans XAMPP.
    echo   Vous devrez recreer vos bases de donnees.
    pause
    exit /b 0
)

if "%OPTION%"=="3" (
    echo.
    echo Ouverture du fichier de configuration my.ini...
    if exist "C:\xampp\mysql\bin\my.ini" (
        notepad "C:\xampp\mysql\bin\my.ini"
    ) else (
        echo ✗ Fichier my.ini non trouve dans C:\xampp\mysql\bin\
        echo   Verifiez votre installation XAMPP.
    )
    pause
    exit /b 0
)

if "%OPTION%"=="4" (
    echo.
    echo Pour reinstaller MySQL dans XAMPP:
    echo   1. Telechargez XAMPP depuis https://www.apachefriends.org/
    echo   2. Desinstallez XAMPP (ou seulement MySQL)
    echo   3. Reinstallez XAMPP
    echo   4. Redemarrez ce script
    echo.
    pause
    exit /b 0
)

if "%OPTION%"=="5" (
    exit /b 0
)

echo.
echo Option invalide.
pause



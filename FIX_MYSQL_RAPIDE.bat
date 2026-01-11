@echo off
chcp 65001 >nul
cls
echo ========================================
echo   REPARATION RAPIDE MYSQL XAMPP
echo ========================================
echo.

echo [1/4] Verification du port 3306...
echo.
netstat -ano | findstr ":3306" >nul
if %errorlevel% == 0 (
    echo ⚠ Port 3306 OCCUPE!
    echo.
    echo Processus utilisant le port:
    netstat -ano | findstr ":3306"
    echo.
    echo Arret des processus MySQL...
    taskkill /F /IM mysqld.exe /T >nul 2>&1
    taskkill /F /IM mysql.exe /T >nul 2>&1
    timeout /t 2 >nul
    echo ✓ Processus arretes
) else (
    echo ✓ Port 3306 disponible
)
echo.

echo [2/4] Verification des logs d'erreur...
echo.
if exist "C:\xampp\mysql\data\*.err" (
    echo ⚠ Erreurs trouvees dans les logs:
    echo.
    for %%f in (C:\xampp\mysql\data\*.err) do (
        echo === Dernieres lignes de %%f ===
        powershell -Command "Get-Content '%%f' -Tail 10 -ErrorAction SilentlyContinue"
        echo.
    )
) else (
    echo ✓ Aucun fichier d'erreur trouve
)
echo.

echo [3/4] Verification des permissions...
echo.
if exist "C:\xampp\mysql\data" (
    echo ✓ Dossier data existe
) else (
    echo ✗ ERREUR: Dossier data manquant!
    echo   Creation du dossier...
    mkdir "C:\xampp\mysql\data" >nul 2>&1
    echo ✓ Dossier cree
)
echo.

echo [4/4] Tentative de demarrage...
echo.
echo Redemarrage de MySQL via XAMPP...
echo.
echo INSTRUCTIONS:
echo   1. Dans le panneau de controle XAMPP, cliquez sur "Stop" pour MySQL
echo   2. Attendez 5 secondes
echo   3. Cliquez sur "Start" pour MySQL
echo   4. Si ca ne fonctionne pas, executez l'option de reinitialisation ci-dessous
echo.
pause

echo.
echo ========================================
echo   OPTIONS AVANCEES
echo ========================================
echo.
echo Voulez-vous reinitialiser MySQL?
echo ⚠ ATTENTION: Cela supprime TOUTES les bases de donnees!
echo.
set /p RESET="Reinitialiser MySQL? (O/N): "

if /i "%RESET%"=="O" (
    echo.
    echo Arret de MySQL...
    if exist "C:\xampp\mysql_stop.bat" (
        call "C:\xampp\mysql_stop.bat" >nul 2>&1
    )
    timeout /t 3 >nul
    
    echo Arret des processus...
    taskkill /F /IM mysqld.exe /T >nul 2>&1
    timeout /t 2 >nul
    
    echo Sauvegarde des donnees existantes...
    if exist "C:\xampp\mysql\data" (
        if not exist "C:\xampp\mysql\data_backup" (
            echo Creation d'une sauvegarde dans data_backup...
            xcopy "C:\xampp\mysql\data" "C:\xampp\mysql\data_backup" /E /I /H /Y >nul 2>&1
        )
        echo Suppression du dossier data...
        rd /s /q "C:\xampp\mysql\data" 2>nul
        timeout /t 1 >nul
    )
    
    echo Creation du nouveau dossier data...
    mkdir "C:\xampp\mysql\data" >nul 2>&1
    
    echo Initialisation de MySQL...
    echo (Cela peut prendre quelques secondes...)
    cd /d "C:\xampp\mysql\bin"
    mysqld.exe --initialize-insecure --datadir=C:\xampp\mysql\data --console >nul 2>&1
    
    if %errorlevel% == 0 (
        echo.
        echo ✓ MySQL reinitialise avec succes!
        echo.
        echo PROCHAINES ETAPES:
        echo   1. Redemarrez MySQL dans le panneau XAMPP
        echo   2. Recreer votre base de donnees avec create_database.sql
        echo   3. Recreez votre administrateur avec CREER_ADMIN.bat
        echo.
    ) else (
        echo.
        echo ✗ ERREUR lors de l'initialisation
        echo   Verifiez que vous avez les droits administrateur
        echo   et que XAMPP est correctement installe.
        echo.
    )
)

echo.
echo ========================================
echo   AIDE SUPPLEMENTAIRE
echo ========================================
echo.
echo Si le probleme persiste:
echo   1. Verifiez les logs dans C:\xampp\mysql\data\*.err
echo   2. Executez XAMPP en tant qu'administrateur
echo   3. Desactivez temporairement votre antivirus
echo   4. Consultez GUIDE_FIX_MYSQL.md pour plus de details
echo.
pause



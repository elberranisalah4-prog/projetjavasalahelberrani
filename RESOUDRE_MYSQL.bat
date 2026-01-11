@echo off
chcp 65001 >nul
cls
echo ========================================
echo   RESOLUTION PROBLEME MYSQL XAMPP
echo ========================================
echo.

echo DIAGNOSTIC EN COURS...
echo.

echo [1] Verification du port 3306...
netstat -ano | findstr ":3306" >nul
if %errorlevel% == 0 (
    echo ⚠ Port 3306 OCCUPE par:
    echo.
    netstat -ano | findstr ":3306"
    echo.
    echo Arret des processus utilisant le port 3306...
    for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":3306" ^| findstr "LISTENING"') do (
        echo   - Arret du processus PID: %%a
        taskkill /F /PID %%a >nul 2>&1
        if !errorlevel! == 0 (
            echo     ✓ Processus arrette
        ) else (
            echo     ✗ Impossible d'arreter (droits admin requis?)
        )
    )
    timeout /t 3 >nul
) else (
    echo ✓ Port 3306 libre
)
echo.

echo [2] Arret de tous les processus MySQL...
taskkill /F /IM mysqld.exe /T >nul 2>&1
taskkill /F /IM mysql.exe /T >nul 2>&1
taskkill /F /FI "WINDOWTITLE eq MySQL*" /T >nul 2>&1
timeout /t 2 >nul
echo ✓ Processus MySQL arretes
echo.

echo [3] Arret via XAMPP...
if exist "C:\xampp\mysql_stop.bat" (
    call "C:\xampp\mysql_stop.bat" >nul 2>&1
    timeout /t 3 >nul
    echo ✓ MySQL arrete via XAMPP
) else (
    echo ⚠ mysql_stop.bat non trouve
)
echo.

echo [4] Verification finale du port...
timeout /t 2 >nul
netstat -ano | findstr ":3306" >nul
if %errorlevel% == 0 (
    echo.
    echo ⚠ ATTENTION: Le port 3306 est ENCORE utilise!
    echo.
    echo Processus restants:
    netstat -ano | findstr ":3306"
    echo.
    echo SOLUTION: Executez ce script en tant qu'ADMINISTRATEUR
    echo (Clic droit sur le fichier -^> Executer en tant qu'administrateur)
    echo.
    pause
    exit /b 1
) else (
    echo ✓ Port 3306 libere avec succes!
)
echo.

echo ========================================
echo   PORT LIBERE - REDEMARRAGE MYSQL
echo ========================================
echo.
echo Instructions:
echo   1. Ouvrez le Panneau de controle XAMPP
echo   2. Cliquez sur "Start" a cote de MySQL
echo   3. Verifiez que MySQL demarre correctement
echo.
echo Si MySQL ne demarre toujours pas:
echo   1. Executez FIX_MYSQL_RAPIDE.bat pour plus d'options
echo   2. Consultez GUIDE_FIX_MYSQL.md pour d'autres solutions
echo.
pause



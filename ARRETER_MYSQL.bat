@echo off
chcp 65001 >nul
echo ========================================
echo   ARRET COMPLET DE MYSQL
echo ========================================
echo.

echo Arret de tous les processus MySQL...
echo.

REM Arrêter MySQL via XAMPP
if exist "C:\xampp\mysql_stop.bat" (
    echo [1] Arret via XAMPP...
    call "C:\xampp\mysql_stop.bat" >nul 2>&1
    timeout /t 2 >nul
)

REM Arrêter tous les processus mysqld.exe
echo [2] Arret des processus mysqld.exe...
taskkill /F /IM mysqld.exe /T >nul 2>&1
if %errorlevel% == 0 (
    echo ✓ Processus mysqld.exe arretes
) else (
    echo ✓ Aucun processus mysqld.exe trouve
)

REM Arrêter tous les processus mysql.exe
echo [3] Arret des processus mysql.exe...
taskkill /F /IM mysql.exe /T >nul 2>&1
if %errorlevel% == 0 (
    echo ✓ Processus mysql.exe arretes
) else (
    echo ✓ Aucun processus mysql.exe trouve
)

echo.
echo [4] Attente de liberation du port 3306...
timeout /t 3 >nul

REM Vérifier si le port est libéré
netstat -ano | findstr ":3306" >nul
if %errorlevel% == 0 (
    echo.
    echo ⚠ Le port 3306 est encore utilise!
    echo.
    echo Processus utilisant le port 3306:
    netstat -ano | findstr ":3306"
    echo.
    echo Tentative d'arret forcee...
    for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":3306" ^| findstr "LISTENING"') do (
        echo Arret du processus PID: %%a
        taskkill /F /PID %%a >nul 2>&1
    )
    timeout /t 2 >nul
) else (
    echo ✓ Port 3306 libere
)

echo.
echo ========================================
echo   ARRET TERMINE
echo ========================================
echo.
echo Vous pouvez maintenant redemarrer MySQL dans XAMPP.
echo.
pause



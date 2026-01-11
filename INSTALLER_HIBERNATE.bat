@echo off
echo ========================================
echo   INSTALLATION HIBERNATE
echo ========================================
echo.
echo Ce script va telecharger les dependances Hibernate necessaires.
echo.

cd /d "%~dp0"

REM Exécuter le script PowerShell
powershell.exe -ExecutionPolicy Bypass -File "%~dp0telecharger-hibernate.ps1"

pause


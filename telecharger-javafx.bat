@echo off
echo ========================================
echo   TELECHARGEMENT AUTOMATIQUE JAVAFX
echo ========================================
echo.
echo Ce script va telecharger et installer JavaFX automatiquement.
echo.
pause

REM Exécuter le script PowerShell
powershell.exe -ExecutionPolicy Bypass -File "%~dp0telecharger-javafx.ps1"

pause


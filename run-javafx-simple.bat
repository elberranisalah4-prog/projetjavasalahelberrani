@echo off
setlocal enabledelayedexpansion

cd /d "%~dp0"
cd ..\..\..

echo ========================================
echo   LANCEMENT JAVAFX - VERSION SIMPLIFIEE
echo ========================================
echo.

REM Lire le chemin JavaFX
set /p JAVAFX_PATH=<javafx_path.txt
set JAVAFX_PATH=!JAVAFX_PATH: =!

echo Chemin JavaFX: !JAVAFX_PATH!
echo.

REM Construire le classpath
set CLASSPATH=com\municipal\dashboard\lib\mysql-connector-j-9.5.0.jar;!JAVAFX_PATH!\javafx.base.jar;!JAVAFX_PATH!\javafx.controls.jar;!JAVAFX_PATH!\javafx.fxml.jar;!JAVAFX_PATH!\javafx.graphics.jar

REM Options JavaFX
set JAVA_OPTS=--module-path "!JAVAFX_PATH!" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base

echo Compilation...
javac -encoding UTF-8 -cp "!CLASSPATH!" com\municipal\dashboard\DashboardApplication.java com\municipal\dashboard\ui\*.java 2>&1 | findstr /V "Note:"

if errorlevel 1 (
    echo ERREUR de compilation!
    pause
    exit /b 1
)

echo.
echo Lancement de l'application...
echo.

java -cp ".;!CLASSPATH!" !JAVA_OPTS! com.municipal.dashboard.DashboardApplication

pause


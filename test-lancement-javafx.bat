@echo off
echo ========================================
echo   TEST DE LANCEMENT JAVAFX
echo ========================================
echo.

cd /d "%~dp0"
cd ..\..\..

setlocal enabledelayedexpansion

set /p JAVAFX_PATH=<javafx_path.txt
set JAVAFX_PATH=!JAVAFX_PATH: =!

echo Chemin JavaFX: !JAVAFX_PATH!
echo.

set CLASSPATH=com\municipal\dashboard\lib\mysql-connector-j-9.5.0.jar;!JAVAFX_PATH!\javafx.base.jar;!JAVAFX_PATH!\javafx.controls.jar;!JAVAFX_PATH!\javafx.fxml.jar;!JAVAFX_PATH!\javafx.graphics.jar

echo Test de compilation simple...
javac -encoding UTF-8 -cp "!CLASSPATH!" com\municipal\dashboard\DashboardApplication.java 2>&1

if errorlevel 1 (
    echo.
    echo ERREUR de compilation!
    pause
    exit /b 1
)

echo.
echo Compilation OK!
echo.
echo Test de lancement...
echo.

set JAVA_OPTS=--module-path "!JAVAFX_PATH!" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base

echo Commande: java -cp ".;!CLASSPATH!" !JAVA_OPTS! com.municipal.dashboard.DashboardApplication
echo.

java -cp ".;!CLASSPATH!" !JAVA_OPTS! com.municipal.dashboard.DashboardApplication 2>&1

if errorlevel 1 (
    echo.
    echo ERREUR lors du lancement!
    pause
    exit /b 1
)

pause


@echo off
echo Test de configuration JavaFX...
echo.

cd /d "%~dp0"
cd ..\..\..

setlocal enabledelayedexpansion

if not exist "javafx_path.txt" (
    echo ERREUR: javafx_path.txt non trouve
    pause
    exit /b 1
)

set /p JAVAFX_PATH=<javafx_path.txt
set JAVAFX_PATH=!JAVAFX_PATH: =!

echo Chemin JavaFX lu: !JAVAFX_PATH!
echo.

if not exist "!JAVAFX_PATH!" (
    echo ERREUR: Le chemin n'existe pas
    pause
    exit /b 1
)

if not exist "!JAVAFX_PATH!\javafx.controls.jar" (
    echo ERREUR: javafx.controls.jar non trouve
    pause
    exit /b 1
)

echo Configuration OK!
echo.
echo Test de compilation...
echo.

set CLASSPATH=com\municipal\dashboard\lib\mysql-connector-j-9.5.0.jar;!JAVAFX_PATH!\javafx.base.jar;!JAVAFX_PATH!\javafx.controls.jar;!JAVAFX_PATH!\javafx.fxml.jar;!JAVAFX_PATH!\javafx.graphics.jar

javac -version
echo.
echo Classpath: !CLASSPATH!
echo.

pause


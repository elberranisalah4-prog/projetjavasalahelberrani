@echo off
echo Compilation du projet Dashboard Services Publics...
echo.

REM Aller au répertoire racine du projet (C:\src\main\java)
cd /d "%~dp0"
cd ..\..\..\..

if not exist "com\municipal\dashboard\lib\mysql-connector-java.jar" (
    echo ATTENTION: Le driver MySQL n'est pas trouve dans le dossier com\municipal\dashboard\lib\
    echo La compilation peut echouer si le driver est necessaire.
)

javac -encoding UTF-8 -cp "com\municipal\dashboard\lib\mysql-connector-j-9.5.0.jar" com\municipal\dashboard\*.java com\municipal\dashboard\dao\*.java com\municipal\dashboard\ui\*.java com\municipal\dashboard\service\*.java com\municipal\dashboard\util\*.java com\municipal\dashboard\repository\*.java com\municipal\dashboard\model\*.java com\municipal\dashboard\exception\*.java

if errorlevel 1 (
    echo ERREUR lors de la compilation!
    pause
    exit /b 1
) else (
    echo.
    echo Compilation reussie!
    echo.
)

pause


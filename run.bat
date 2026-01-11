@echo off
echo ========================================
echo   DASHBOARD SERVICES PUBLICS
echo   Lancement de l'application...
echo ========================================
echo.

REM Aller au répertoire racine du projet (C:\src\main\java)
cd /d "%~dp0"
cd ..\..\..

if not exist "com\municipal\dashboard\lib\mysql-connector-j-9.5.0.jar" (
    echo ERREUR: Le driver MySQL n'est pas trouve dans le dossier com\municipal\dashboard\lib\
    echo Veuillez verifier que mysql-connector-j-9.5.0.jar est present
    pause
    exit /b 1
)

echo Compilation en cours (exclusion des fichiers JavaFX)...
javac -encoding UTF-8 -cp "com\municipal\dashboard\lib\mysql-connector-j-9.5.0.jar" ^
com\municipal\dashboard\Admin.java ^
com\municipal\dashboard\AuthManager.java ^
com\municipal\dashboard\Consommation.java ^
com\municipal\dashboard\CreateAdmin.java ^
com\municipal\dashboard\CreateAdminDirect.java ^
com\municipal\dashboard\Database.java ^
com\municipal\dashboard\DatabaseConfig.java ^
com\municipal\dashboard\DatabaseManager.java ^
com\municipal\dashboard\Decision.java ^
com\municipal\dashboard\DeleteAdmin.java ^
com\municipal\dashboard\DeleteAdminDirect.java ^
com\municipal\dashboard\FixAdmin.java ^
com\municipal\dashboard\Indicator.java ^
com\municipal\dashboard\ListAdmins.java ^
com\municipal\dashboard\Main.java ^
com\municipal\dashboard\MainApp.java ^
com\municipal\dashboard\MenuPrincipal.java ^
com\municipal\dashboard\MigrateToPhpMyAdmin.java ^
com\municipal\dashboard\Note.java ^
com\municipal\dashboard\Personnel.java ^
com\municipal\dashboard\Poste.java ^
com\municipal\dashboard\Prediction.java ^
com\municipal\dashboard\Production.java ^
com\municipal\dashboard\RapportFormulaires.java ^
com\municipal\dashboard\Service.java ^
com\municipal\dashboard\TestDatabaseInsert.java ^
com\municipal\dashboard\TestDataDisplay.java ^
com\municipal\dashboard\TestLogin.java ^
com\municipal\dashboard\TestMySQL.java ^
com\municipal\dashboard\TestVerification.java ^
com\municipal\dashboard\VerifierFormulaires.java ^
com\municipal\dashboard\Zone.java ^
com\municipal\dashboard\dao\AdminDAO.java ^
com\municipal\dashboard\dao\ConsommationDAO.java ^
com\municipal\dashboard\dao\ConsommationDAOAsync.java ^
com\municipal\dashboard\dao\DecisionDAO.java ^
com\municipal\dashboard\dao\IndicatorDAO.java ^
com\municipal\dashboard\dao\NoteDAO.java ^
com\municipal\dashboard\dao\PersonnelDAO.java ^
com\municipal\dashboard\dao\PosteDAO.java ^
com\municipal\dashboard\dao\PredictionDAO.java ^
com\municipal\dashboard\dao\ProductionDAO.java ^
com\municipal\dashboard\dao\ServiceDAO.java ^
com\municipal\dashboard\dao\ZoneDAO.java ^
com\municipal\dashboard\service\*.java ^
com\municipal\dashboard\util\CollectionUtils.java ^
com\municipal\dashboard\util\GenericManager.java ^
com\municipal\dashboard\util\OptionalUtils.java ^
com\municipal\dashboard\util\StreamUtils.java ^
com\municipal\dashboard\util\ThreadPoolManager.java ^
com\municipal\dashboard\repository\*.java ^
com\municipal\dashboard\model\*.java ^
com\municipal\dashboard\exception\*.java

if errorlevel 1 (
    echo ERREUR lors de la compilation!
    pause
    exit /b 1
)

echo.
echo Lancement de l'application...
echo.
java -cp ".;com\municipal\dashboard\lib\mysql-connector-j-9.5.0.jar" com.municipal.dashboard.Main

pause


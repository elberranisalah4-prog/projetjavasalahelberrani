Write-Host ""
Write-Host "========================================"
Write-Host "  CRÉATION D'ADMINISTRATEUR"
Write-Host "  Dashboard Services Publics"
Write-Host "========================================"
Write-Host ""

# Aller au répertoire racine du projet (C:\src\main\java)
$scriptPath = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $scriptPath
Set-Location ..\..\..

if (-not (Test-Path "com\municipal\dashboard\lib\mysql-connector-java.jar")) {
    Write-Host "ERREUR: Le driver MySQL n'est pas trouve dans le dossier com\municipal\dashboard\lib\" -ForegroundColor Red
    Write-Host "Veuillez telecharger mysql-connector-java.jar et le placer dans com\municipal\dashboard\lib\" -ForegroundColor Red
    Read-Host "Appuyez sur Entree pour continuer"
    exit 1
}

Write-Host "Compilation en cours..."
javac -encoding UTF-8 -cp "com\municipal\dashboard\lib\mysql-connector-java.jar" com\municipal\dashboard\*.java com\municipal\dashboard\dao\*.java 2>&1 | Out-Null

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERREUR lors de la compilation!" -ForegroundColor Red
    Read-Host "Appuyez sur Entree pour continuer"
    exit 1
}

Write-Host ""
Write-Host "Veuillez remplir les informations de l'administrateur:"
Write-Host ""

$nom = Read-Host "Nom *"
$prenom = Read-Host "Prenom *"
$email = Read-Host "Email *"
$motdepasse = Read-Host "Mot de passe *" -AsSecureString
$telephone = Read-Host "Telephone (optionnel - appuyez sur Entree pour ignorer)"

# Convertir le SecureString en texte
$BSTR = [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($motdepasse)
$motdepassePlain = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto($BSTR)

Write-Host ""
Write-Host "Creation de l'administrateur en cours..."
Write-Host ""

if ($telephone -eq "") {
    java -cp ".;com\municipal\dashboard\lib\mysql-connector-java.jar" com.municipal.dashboard.CreateAdminDirect $nom $prenom $email $motdepassePlain
} else {
    java -cp ".;com\municipal\dashboard\lib\mysql-connector-java.jar" com.municipal.dashboard.CreateAdminDirect $nom $prenom $email $motdepassePlain $telephone
}

Write-Host ""
Read-Host "Appuyez sur Entree pour continuer"


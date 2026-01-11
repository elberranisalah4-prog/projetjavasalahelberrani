# Script PowerShell pour télécharger et installer JavaFX automatiquement
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  TELECHARGEMENT AUTOMATIQUE JAVAFX" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$javaVersion = (java -version 2>&1 | Select-String "version").ToString().Split('"')[1]
Write-Host "Version Java detectee: $javaVersion" -ForegroundColor Green

# Extraire la version majeure
if ($javaVersion -match "(\d+)\.") {
    $javaMajorVersion = $matches[1]
    Write-Host "Version majeure Java: $javaMajorVersion" -ForegroundColor Green
} else {
    $javaMajorVersion = "17"
    Write-Host "Impossible de determiner la version, utilisation de JavaFX 17 par defaut" -ForegroundColor Yellow
}

# Définir l'URL de téléchargement GitHub pour OpenJFX
$javafxVersion = "17.0.10"
$downloadUrl = "https://github.com/openjfx/javafx-binaries/releases/download/${javafxVersion}/openjfx-${javafxVersion}_windows-x64_bin-sdk.zip"
$zipFile = "openjfx-${javafxVersion}_windows-x64_bin-sdk.zip"
$extractPath = "C:\javafx-sdk-${javafxVersion}"
$libPath = "$extractPath\lib"

Write-Host ""
Write-Host "JavaFX sera installe dans: $extractPath" -ForegroundColor Yellow
Write-Host ""

# Demander confirmation
$confirm = Read-Host "Voulez-vous telecharger JavaFX $javafxVersion? (O/N)"
if ($confirm -ne "O" -and $confirm -ne "o") {
    Write-Host "Telechargement annule." -ForegroundColor Red
    exit
}

# Créer le dossier de téléchargement temporaire
$tempPath = $env:TEMP
$zipPath = Join-Path $tempPath $zipFile

Write-Host ""
Write-Host "Telechargement en cours..." -ForegroundColor Yellow
Write-Host "URL: $downloadUrl" -ForegroundColor Gray
Write-Host "Fichier: $zipPath" -ForegroundColor Gray
Write-Host ""

try {
    # Télécharger JavaFX
    Invoke-WebRequest -Uri $downloadUrl -OutFile $zipPath -UseBasicParsing
    
    if (Test-Path $zipPath) {
        Write-Host "Telechargement termine!" -ForegroundColor Green
        Write-Host ""
        
        # Extraire l'archive
        Write-Host "Extraction en cours..." -ForegroundColor Yellow
        if (Test-Path $extractPath) {
            Remove-Item -Path $extractPath -Recurse -Force
        }
        
        Expand-Archive -Path $zipPath -DestinationPath $extractPath -Force
        Write-Host "Extraction terminee!" -ForegroundColor Green
        Write-Host ""
        
        # Vérifier que le dossier lib existe
        if (Test-Path $libPath) {
            Write-Host "JavaFX installe avec succes!" -ForegroundColor Green
            Write-Host "Chemin lib: $libPath" -ForegroundColor Green
            Write-Host ""
            
            # Sauvegarder le chemin dans javafx_path.txt
            $configPath = Join-Path (Get-Location) "javafx_path.txt"
            $libPath | Out-File -FilePath $configPath -Encoding ASCII -NoNewline
            Write-Host "Configuration sauvegardee dans: $configPath" -ForegroundColor Green
            Write-Host ""
            
            Write-Host "========================================" -ForegroundColor Cyan
            Write-Host "  INSTALLATION TERMINEE!" -ForegroundColor Green
            Write-Host "========================================" -ForegroundColor Cyan
            Write-Host ""
            Write-Host "Vous pouvez maintenant lancer l'application avec:" -ForegroundColor Yellow
            Write-Host "  run-javafx.bat" -ForegroundColor White
            Write-Host ""
            
            # Nettoyer le fichier ZIP
            $cleanup = Read-Host "Voulez-vous supprimer le fichier ZIP telecharge? (O/N)"
            if ($cleanup -eq "O" -or $cleanup -eq "o") {
                Remove-Item -Path $zipPath -Force
                Write-Host "Fichier ZIP supprime." -ForegroundColor Green
            }
        } else {
            Write-Host "ERREUR: Le dossier lib n'a pas ete cree!" -ForegroundColor Red
        }
    } else {
        Write-Host "ERREUR: Le telechargement a echoue!" -ForegroundColor Red
    }
} catch {
    Write-Host ""
    Write-Host "ERREUR lors du telechargement:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    Write-Host ""
    Write-Host "Vous pouvez telecharger manuellement JavaFX depuis:" -ForegroundColor Yellow
    Write-Host "https://openjfx.io/" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Puis executez 'configurerJavaFX.bat' pour configurer le chemin." -ForegroundColor Yellow
}

Write-Host ""
Write-Host "Appuyez sur une touche pour continuer..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")


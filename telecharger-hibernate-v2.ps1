# Script PowerShell pour télécharger Hibernate automatiquement
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  TELECHARGEMENT HIBERNATE" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$hibernateVersion = "6.4.4.Final"
$libDir = "lib"

# Créer le dossier lib s'il n'existe pas
if (-not (Test-Path $libDir)) {
    New-Item -ItemType Directory -Path $libDir -Force | Out-Null
    Write-Host "Dossier $libDir cree" -ForegroundColor Green
}

Write-Host "Version Hibernate: $hibernateVersion" -ForegroundColor Yellow
Write-Host "Dossier de destination: $libDir" -ForegroundColor Yellow
Write-Host ""

# Liste des dépendances nécessaires avec URLs directes
$dependencies = @(
    @{
        Name = "hibernate-core"
        URL = "https://repo1.maven.org/maven2/org/hibernate/orm/hibernate-core/$hibernateVersion/hibernate-core-$hibernateVersion.jar"
        FileName = "hibernate-core-$hibernateVersion.jar"
    },
    @{
        Name = "jakarta.persistence-api"
        URL = "https://repo1.maven.org/maven2/jakarta/persistence/jakarta.persistence-api/3.1.0/jakarta.persistence-api-3.1.0.jar"
        FileName = "jakarta.persistence-api-3.1.0.jar"
    },
    @{
        Name = "jboss-logging"
        URL = "https://repo1.maven.org/maven2/org/jboss/logging/jboss-logging/3.5.3.Final/jboss-logging-3.5.3.Final.jar"
        FileName = "jboss-logging-3.5.3.Final.jar"
    },
    @{
        Name = "antlr"
        URL = "https://repo1.maven.org/maven2/org/antlr/antlr-runtime/3.5.3/antlr-runtime-3.5.3.jar"
        FileName = "antlr-runtime-3.5.3.jar"
    },
    @{
        Name = "byte-buddy"
        URL = "https://repo1.maven.org/maven2/net/bytebuddy/byte-buddy/1.14.13/byte-buddy-1.14.13.jar"
        FileName = "byte-buddy-1.14.13.jar"
    },
    @{
        Name = "javassist"
        URL = "https://repo1.maven.org/maven2/org/javassist/javassist/3.30.2-GA/javassist-3.30.2-GA.jar"
        FileName = "javassist-3.30.2-GA.jar"
    }
)

$confirm = Read-Host "Voulez-vous telecharger Hibernate $hibernateVersion? (O/N)"
if ($confirm -ne "O" -and $confirm -ne "o") {
    Write-Host "Telechargement annule." -ForegroundColor Red
    exit
}

Write-Host ""
Write-Host "Telechargement des dependances..." -ForegroundColor Yellow
Write-Host ""

$successCount = 0
foreach ($dep in $dependencies) {
    $url = $dep.URL
    $fileName = $dep.FileName
    $depName = $dep.Name
    $outputFile = Join-Path $libDir $fileName
    
    Write-Host "Telechargement: $depName..." -ForegroundColor Cyan
    Write-Host "  URL: $url" -ForegroundColor Gray
    
    try {
        Invoke-WebRequest -Uri $url -OutFile $outputFile -UseBasicParsing
        if (Test-Path $outputFile) {
            $fileSize = (Get-Item $outputFile).Length / 1MB
            $fileSizeFormatted = [math]::Round($fileSize, 2)
            Write-Host "  [OK] Telecharge: $fileName ($fileSizeFormatted MB)" -ForegroundColor Green
            $successCount++
        }
    } catch {
        Write-Host "  [ERREUR] Erreur lors du telechargement de $depName" -ForegroundColor Red
        Write-Host "    $($_.Exception.Message)" -ForegroundColor Red
        Write-Host "    URL testee: $url" -ForegroundColor Gray
    }
    Write-Host ""
}

if ($successCount -eq $dependencies.Length) {
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host "  TELECHARGEMENT TERMINE!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "$successCount fichier(s) telecharge(s) dans $libDir" -ForegroundColor Green
    Write-Host ""
    Write-Host "IMPORTANT: Vous devez aussi ajouter ces fichiers au classpath lors de la compilation:" -ForegroundColor Yellow
    Write-Host "  javac -cp `"lib\*.jar;com\municipal\dashboard\lib\mysql-connector-j-9.5.0.jar`" ..." -ForegroundColor White
    Write-Host ""
} else {
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host "  TELECHARGEMENT PARTIEL" -ForegroundColor Yellow
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "$successCount sur $($dependencies.Length) fichiers telecharges" -ForegroundColor Yellow
    Write-Host ""
    if ($successCount -gt 0) {
        Write-Host "Les fichiers telecharges sont disponibles dans $libDir" -ForegroundColor Green
        Write-Host ""
        Write-Host "Pour les fichiers manquants, vous pouvez les telecharger manuellement depuis:" -ForegroundColor Yellow
        Write-Host "https://repo1.maven.org/maven2/org/hibernate/orm/hibernate-core/" -ForegroundColor Cyan
    }
}

Write-Host ""
Write-Host "Appuyez sur une touche pour continuer..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")


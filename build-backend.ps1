# Compile and test Java backend for Rungta R1 Smart Campus Navigation
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "  Compiling Rungta Campus Java Backend...          " -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Cyan

if (!(Test-Path "backend\bin")) {
    New-Item -ItemType Directory -Force -Path "backend\bin" | Out-Null
}

$javaPath = if (Test-Path "C:\Program Files\Java\jdk-25.0.2\bin\javac.exe") {
    "C:\Program Files\Java\jdk-25.0.2\bin\javac.exe"
} else {
    "javac"
}

$runPath = if (Test-Path "C:\Program Files\Java\jdk-25.0.2\bin\java.exe") {
    "C:\Program Files\Java\jdk-25.0.2\bin\java.exe"
} else {
    "java"
}

$sourceFiles = Get-ChildItem -Recurse -Filter *.java backend\src | ForEach-Object { $_.FullName }
& $javaPath -encoding UTF-8 -d backend\bin $sourceFiles

if ($LASTEXITCODE -eq 0) {
    Write-Host "`n[SUCCESS] Backend compiled with 0 errors!" -ForegroundColor Green
    Write-Host "Running automated test suite...`n" -ForegroundColor Yellow
    & $runPath "-Dfile.encoding=UTF-8" -cp backend\bin com.rungta.campus.RungtaCampusTest
} else {
    Write-Host "`n[FAILED] Backend compilation failed with code $LASTEXITCODE" -ForegroundColor Red
}

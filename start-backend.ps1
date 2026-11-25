# Launch Rungta Campus Navigation Backend Server on port 8080
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "  Starting Rungta Campus Java REST API Server...   " -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Cyan

if (!(Test-Path "backend\bin\com\rungta\campus\RungtaServer.class")) {
    Write-Host "Compiling backend first..." -ForegroundColor Yellow
    & ".\build-backend.ps1"
}

$runPath = if (Test-Path "C:\Program Files\Java\jdk-25.0.2\bin\java.exe") {
    "C:\Program Files\Java\jdk-25.0.2\bin\java.exe"
} else {
    "java"
}

& $runPath "-Dfile.encoding=UTF-8" -cp backend\bin com.rungta.campus.RungtaServer 8080

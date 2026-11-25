# PowerShell launch script for Campus Navigator GUI Dashboard
param(
    [Parameter(ValueFromRemainingArguments = $true)]
    [string[]]$AppArgs
)

Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "  Starting Campus Navigator Application...         " -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Cyan

if (!(Test-Path "bin\com\campus\navigator\CampusNavigationApp.class")) {
    Write-Host "Binaries not found. Running compilation first..." -ForegroundColor Yellow
    & ".\compile.ps1"
}

$javaPath = if (Test-Path "C:\Program Files\Java\jdk-25.0.2\bin\java.exe") {
    "C:\Program Files\Java\jdk-25.0.2\bin\java.exe"
} elseif ($env:JAVA_HOME -and (Test-Path "$env:JAVA_HOME\bin\java.exe")) {
    "$env:JAVA_HOME\bin\java.exe"
} else {
    "java"
}

& $javaPath "-Dfile.encoding=UTF-8" -cp bin com.campus.navigator.CampusNavigationApp $AppArgs

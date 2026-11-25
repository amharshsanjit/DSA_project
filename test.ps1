# PowerShell test suite runner for Campus Navigator
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "  Running Campus Navigator Automated Test Suite... " -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Cyan

if (!(Test-Path "bin\com\campus\navigator\test\CampusNavigationTest.class")) {
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

& $javaPath "-Dfile.encoding=UTF-8" -cp bin com.campus.navigator.test.CampusNavigationTest

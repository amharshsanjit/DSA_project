# PowerShell compilation script for Campus Navigator
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "  Compiling Campus Navigation Application...       " -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Cyan

if (!(Test-Path "bin")) {
    New-Item -ItemType Directory -Force -Path "bin" | Out-Null
}

$javaPath = if (Test-Path "C:\Program Files\Java\jdk-25.0.2\bin\javac.exe") {
    "C:\Program Files\Java\jdk-25.0.2\bin\javac.exe"
} elseif ($env:JAVA_HOME -and (Test-Path "$env:JAVA_HOME\bin\javac.exe")) {
    "$env:JAVA_HOME\bin\javac.exe"
} else {
    "javac"
}

$sources = Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName }
& $javaPath -encoding UTF-8 -d bin $sources

if ($LASTEXITCODE -eq 0) {
    Write-Host "`n[SUCCESS] Compilation completed with 0 errors! Class files generated in 'bin'." -ForegroundColor Green
} else {
    Write-Host "`n[FAILED] Compilation failed with error code $LASTEXITCODE." -ForegroundColor Red
}

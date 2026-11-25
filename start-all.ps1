# Launch full-stack Rungta R1 Smart Campus Navigation System
Write-Host "====================================================================" -ForegroundColor Cyan
Write-Host "  Launching Rungta R1 Smart Campus Navigation System (Full-Stack)   " -ForegroundColor Cyan
Write-Host "====================================================================" -ForegroundColor Cyan

Write-Host "1. Starting Java REST API Server (Port 8080)..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-ExecutionPolicy Bypass -NoExit -File `"$PSScriptRoot\start-backend.ps1`""

Start-Sleep -Seconds 2

Write-Host "2. Starting React + Vite Frontend (Port 3000)..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-ExecutionPolicy Bypass -NoExit -File `"$PSScriptRoot\start-frontend.ps1`""

Start-Sleep -Seconds 3

Write-Host "3. Opening Browser at http://localhost:3000..." -ForegroundColor Green
Start-Process "http://localhost:3000"

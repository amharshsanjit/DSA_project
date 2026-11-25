@echo off
cd /d "%~dp0"

echo ====================================================================
echo   Launching Rungta R1 Smart Campus Navigation System (Full-Stack)
echo ====================================================================

echo 1. Starting Java REST API Server (Port 8080)...
start "Rungta Backend (Port 8080)" cmd /k "start-backend.bat"

timeout /t 2 >nul

echo 2. Starting React + Vite Frontend (Port 3000)...
start "Rungta Frontend (Port 3000)" cmd /k "start-frontend.bat"

timeout /t 3 >nul

echo 3. Opening Browser at http://localhost:3000...
start http://localhost:3000

@echo off
cd /d "%~dp0\frontend"

echo ===================================================
echo   Starting Rungta Campus Navigation Frontend...
echo ===================================================

echo Launching React + Vite on http://localhost:3000
npm run dev -- --host
pause

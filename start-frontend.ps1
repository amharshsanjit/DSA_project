# Launch React TypeScript Frontend on port 3000
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "  Starting Rungta Campus React Frontend...         " -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Cyan

Set-Location -Path "$PSScriptRoot\frontend"
& cmd.exe /c "npm run dev -- --host"

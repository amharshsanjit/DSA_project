@echo off
cd /d "%~dp0"

echo ===================================================
echo   Starting Rungta Campus Navigation Backend...
echo ===================================================

set "JAVA_CMD=java"
if exist "C:\Program Files\Java\jdk-25.0.2\bin\java.exe" (
    set "JAVA_CMD=C:\Program Files\Java\jdk-25.0.2\bin\java.exe"
)

if not exist "backend\bin\com\rungta\campus\RungtaServer.class" (
    echo Compiling backend first...
    powershell -ExecutionPolicy Bypass -File .\build-backend.ps1
)

echo Launching Java REST API Server on port 8080...
"%JAVA_CMD%" -Dfile.encoding=UTF-8 -cp backend\bin com.rungta.campus.RungtaServer 8080
pause

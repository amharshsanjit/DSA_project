@echo off
setlocal enabledelayedexpansion

:: Ensure current working directory is the folder where this batch file is located
cd /d "%~dp0"

echo ===================================================
echo   Starting Campus Navigator GUI Application...
echo ===================================================

:: Locate java binary
set "JAVA_CMD=java"
if exist "C:\Program Files\Java\jdk-25.0.2\bin\java.exe" (
    set "JAVA_CMD=C:\Program Files\Java\jdk-25.0.2\bin\java.exe"
) else if defined JAVA_HOME (
    if exist "%JAVA_HOME%\bin\java.exe" set "JAVA_CMD=%JAVA_HOME%\bin\java.exe"
)

:: Compile if bin is missing
if not exist "bin\com\campus\navigator\CampusNavigationApp.class" (
    echo Binaries not found. Running compilation first...
    call compile.bat
)

echo Launching Campus Navigator Dashboard...
"%JAVA_CMD%" -Dfile.encoding=UTF-8 -cp bin com.campus.navigator.CampusNavigationApp %*

if %ERRORLEVEL% neq 0 (
    echo.
    echo Application exited with error code %ERRORLEVEL%.
    pause
)

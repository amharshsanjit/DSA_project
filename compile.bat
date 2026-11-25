@echo off
setlocal enabledelayedexpansion

echo ===================================================
echo   Compiling Campus Navigation Application...
echo ===================================================

if not exist "bin" mkdir bin

:: Find javac binary
set "JAVAC_CMD=javac"
if exist "C:\Program Files\Java\jdk-25.0.2\bin\javac.exe" (
    set "JAVAC_CMD=C:\Program Files\Java\jdk-25.0.2\bin\javac.exe"
) else if defined JAVA_HOME (
    if exist "%JAVA_HOME%\bin\javac.exe" set "JAVAC_CMD=%JAVA_HOME%\bin\javac.exe"
)

powershell -NoProfile -ExecutionPolicy Bypass -Command "$sources = Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName }; & '%JAVAC_CMD%' -encoding UTF-8 -d bin $sources"

set COMPILE_STATUS=%ERRORLEVEL%

if %COMPILE_STATUS% equ 0 (
    echo.
    echo [SUCCESS] Compilation completed with 0 errors! Class files generated in 'bin'.
) else (
    echo.
    echo [FAILED] Compilation failed with error code %COMPILE_STATUS%.
)

exit /b %COMPILE_STATUS%

@echo off
setlocal enabledelayedexpansion

echo ===================================================
echo   Running Campus Navigator Automated Test Suite...
echo ===================================================

:: Find java binary
set "JAVA_CMD=java"
if exist "C:\Program Files\Java\jdk-25.0.2\bin\java.exe" (
    set "JAVA_CMD=C:\Program Files\Java\jdk-25.0.2\bin\java.exe"
) else if defined JAVA_HOME (
    if exist "%JAVA_HOME%\bin\java.exe" set "JAVA_CMD=%JAVA_HOME%\bin\java.exe"
)

if not exist "bin\com\campus\navigator\test\CampusNavigationTest.class" (
    echo Binaries not found. Running compilation first...
    call compile.bat
)

"%JAVA_CMD%" -Dfile.encoding=UTF-8 -cp bin com.campus.navigator.test.CampusNavigationTest

@echo off
call ../gradlew clean build -x test -p ../
set /A status=%errorlevel%
if %status% EQU 0 (
    echo Jar build succeeded
    call docker build -f ../Dockerfile --target dev -t changelog-service:latest-dev ../
    call docker-compose up -d changelog
) else (
    echo Exiting build with error
    exit /b %status%
)
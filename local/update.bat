@echo off
call ../gradlew clean build -x test -p ../
pause
call docker build -f ../Dockerfile --target dev -t changelog-service:latest-dev ../
call docker-compose up -d changelog
@echo off
cd ..

call ./gradlew -x test clean build
cd local
pause

cd ..
call docker build --target dev -t changelog-service:latest-dev .
cd local

call docker-compose up -d changelog
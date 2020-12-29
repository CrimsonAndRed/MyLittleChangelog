#!/bin/bash

../gradlew clean build -x test -p ../
status=$?
if [ $status -eq 0 ]
then
  echo "Jar build succeeded"
  docker build -f ../Dockerfile --target dev -t changelog-service:latest-dev ../
  docker-compose up -d changelog
else
  echo "Exiting build with error"
  exit $status
fi
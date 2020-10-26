../gradlew clean build -x test -p ../
#read -r -p "Press enter to continue"
docker build -f ../Dockerfile --target dev -t changelog-service:latest-dev ../
docker-compose up -d changelog

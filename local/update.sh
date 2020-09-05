cd ..

./gradlew clean build
cd local || exit
read -r -p "Press enter to continue"

cd ..
docker build --target dev -t changelog-service:latest-dev .
cd local || exit

docker-compose up -d changelog

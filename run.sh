echo "Gradle Clean"
./gradlew clean
sleep 5
echo "Gradle Build"
./gradlew build

echo "Build docker images and run"
pushd docker/prod || exit
docker-compose up -d --build
popd || exit

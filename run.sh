echo "Gradle Build Docker image"
./gradlew jibDockerBuild

echo "Build docker images and run"
pushd docker/submit || exit
docker-compose up -d --build
popd || exit

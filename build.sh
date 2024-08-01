
cd order-service
./gradlew build
cd ..
cd orchestrator-service
./gradlew build
cd ..
cd product-validation-service
./gradlew build
cd ..
cd payment-service
./gradlew build
cd ..
cd inventory-service
./gradlew build
cd ..
docker compose down
docker container stop $(docker ps -aq)
docker compose up --build -d
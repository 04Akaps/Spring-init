#!/bin/bash

echo "Starting build process..."

./gradlew build

# 빌드가 성공했는지 확인
if [ $? -eq 0 ]; then
    echo "Build successful. Starting application..."
    cd ./build/libs
    java -jar demo-0.0.1-SNAPSHOT.jar

    # ./gradlew bootRun
else
    echo "Build failed. Skipping application start."
    exit 1
fi
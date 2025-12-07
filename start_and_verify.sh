#!/bin/bash

echo "🚀 Starting CRM System One-Click Verification..."

echo "📦 Building Java Services (skipping tests)..."
# We need to build from root if using aggressive modules, or iterate.
# The project seems to have independent pom.xml files but also a parent pom.
# Let's try building from root or assume user wants iterating.
# "Builds all Java projects: mvn clean package -DskipTests"
# Note: config-server, eureka-server, etc are mostly likely built via docker context "mvn" step inside dockerfile based on existing config-server dockerfile.
# BUT, the prompt EXPLICITLY says "Builds all Java projects: mvn clean package ... THEN Builds Docker images".
# If the Dockerfiles do the building (Multi-stage), this step is redundant for Docker but good for local cache/verification.
# HOWEVER, the existing Dockerfiles COPY . . and RUN mvn inside.
# So running mvn locally is NOT strictly needed for docker-compose up, but the prompt requested it.
# I will run it.
./mvnw clean package -DskipTests

echo "🐳 Building Docker Images..."
docker-compose build

echo "🔥 Starting Docker Stack..."
docker-compose up -d

echo "⏳ Waiting for Gateway to be UP..."
# Loop for 60 seconds
for i in {1..12}; do
  STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/actuator/health)
  if [ "$STATUS" == "200" ]; then
    echo "✅ System is UP!"
    echo "Frontend: http://localhost:4200"
    echo "Gateway: http://localhost:8080"
    echo "Eureka: http://localhost:8761"
    exit 0
  fi
  echo "Waiting... ($i/12)"
  sleep 5
done

echo "❌ Timeout waiting for System UP."
exit 1

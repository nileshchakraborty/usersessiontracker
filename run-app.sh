#!/bin/bash

# Exit on any error
set -e

echo "Building application..."
./mvnw clean package -DskipTests

echo "Building Docker image..."
docker build -t user-session-tracker:latest .

echo "Creating Kubernetes resources..."
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secret.yaml
kubectl apply -f k8s/postgres.yaml
kubectl apply -f k8s/app.yaml

echo "Waiting for PostgreSQL to be ready..."
kubectl wait --for=condition=ready pod -l app=postgres --timeout=120s

echo "Waiting for application to be ready..."
kubectl wait --for=condition=ready pod -l app=user-session-tracker --timeout=120s

echo "Getting service URL..."
minikube service user-session-tracker --url

echo "Application is ready!"
echo "You can access the application at the URL above"
echo "Default credentials: admin/admin" 
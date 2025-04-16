# User Session Tracker

A Spring Boot application for tracking user sessions with AWS integration.

## Features

- User session tracking with login/logout events
- AWS services integration:
  - DynamoDB for session storage
  - SQS for message queuing
  - Kinesis Firehose for data streaming
  - S3 for data storage
  - Athena for analytics
  - Aurora PostgreSQL for relational data
- Multiple deployment options:
  - EKS (Kubernetes)
  - ECS (Fargate)
  - Lambda functions
- Comprehensive testing suite with LocalStack

## Prerequisites

- Java 17
- Maven 3.8+
- Docker
- AWS CLI
- Terraform
- kubectl (for EKS deployment)
- Node.js 16+ (for serverless plugins)

## Setup

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/user-session-tracker.git
cd user-session-tracker
```

### 2. Install Dependencies

```bash
# Install Maven dependencies
mvn clean install

# Install serverless plugins
npm install
```

### 3. Configure AWS Credentials

```bash
aws configure
```

### 4. Local Development Setup

#### Using LocalStack

1. Start LocalStack:
```bash
docker run --rm -it -p 4566:4566 -p 4510-4559:4510-4559 localstack/localstack:3.0.2
```

2. Configure environment variables:
```bash
cp .env.example .env
# Edit .env with your configuration
```

### 5. Infrastructure Setup

#### Using Terraform

```bash
cd infrastructure
terraform init
terraform plan
terraform apply
```

## Running the Application

### Local Development

```bash
# Run with LocalStack
mvn spring-boot:run -Dspring.profiles.active=local

# Run tests
mvn test
mvn test -P integration
```

### Serverless Deployment

```bash
# Deploy to AWS
serverless deploy

# Deploy specific function
serverless deploy function -f processSession
```

### EKS Deployment

1. Build Docker image:
```bash
docker build -t user-session-tracker .
```

2. Push to ECR:
```bash
aws ecr get-login-password | docker login --username AWS --password-stdin ${ECR_REPOSITORY}
docker tag user-session-tracker:latest ${ECR_REPOSITORY}:latest
docker push ${ECR_REPOSITORY}:latest
```

3. Deploy to EKS:
```bash
kubectl apply -f kubernetes/deployment.yaml
```

## Testing

### Unit Tests

```bash
mvn test
```

### Integration Tests

```bash
mvn test -P integration
```

### LocalStack Tests

```bash
mvn test -P localstack
```

## Git Hooks

The project includes pre-commit hooks for:
- Code formatting
- Linting
- Test execution
- Dependency updates

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 
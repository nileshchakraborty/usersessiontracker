terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

# DynamoDB Table
resource "aws_dynamodb_table" "user_sessions" {
  name           = "user_sessions"
  billing_mode   = "PROVISIONED"
  read_capacity  = 5
  write_capacity = 5
  hash_key       = "sessionId"

  attribute {
    name = "sessionId"
    type = "S"
  }
}

# S3 Bucket
resource "aws_s3_bucket" "user_session_data" {
  bucket = "user-session-data"
}

resource "aws_s3_bucket_versioning" "user_session_data" {
  bucket = aws_s3_bucket.user_session_data.id
  versioning_configuration {
    status = "Enabled"
  }
}

# Kinesis Firehose
resource "aws_kinesis_firehose_delivery_stream" "user_session_stream" {
  name        = "user-session-stream"
  destination = "s3"

  s3_configuration {
    role_arn   = aws_iam_role.firehose_role.arn
    bucket_arn = aws_s3_bucket.user_session_data.arn
  }
}

# SQS Queues
resource "aws_sqs_queue" "user_session_queue" {
  name                      = "user-session-queue"
  delay_seconds             = 0
  max_message_size          = 262144
  message_retention_seconds = 86400
  receive_wait_time_seconds = 0
}

resource "aws_sqs_queue" "user_session_dlq" {
  name                      = "user-session-dlq"
  delay_seconds             = 0
  max_message_size          = 262144
  message_retention_seconds = 86400
  receive_wait_time_seconds = 0
}

# Athena Database
resource "aws_athena_database" "user_session_db" {
  name   = "user_session_db"
  bucket = aws_s3_bucket.user_session_data.bucket
}

# Glue Database
resource "aws_glue_catalog_database" "user_session_db" {
  name = "user_session_db"
}

# Aurora PostgreSQL
resource "aws_rds_cluster" "user_session_db" {
  cluster_identifier      = "user-session-db"
  engine                 = "aurora-postgresql"
  engine_version         = "14.7"
  database_name          = "user_session_db"
  master_username        = var.db_username
  master_password        = var.db_password
  skip_final_snapshot    = true
  vpc_security_group_ids = [aws_security_group.rds.id]
}

# IAM Roles
resource "aws_iam_role" "firehose_role" {
  name = "firehose_role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "firehose.amazonaws.com"
        }
      }
    ]
  })
}

# Security Groups
resource "aws_security_group" "rds" {
  name        = "rds-security-group"
  description = "Security group for RDS"

  ingress {
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# Variables
variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "us-east-1"
}

variable "db_username" {
  description = "Database username"
  type        = string
}

variable "db_password" {
  description = "Database password"
  type        = string
  sensitive   = true
}

# Outputs
output "dynamodb_table_name" {
  value = aws_dynamodb_table.user_sessions.name
}

output "s3_bucket_name" {
  value = aws_s3_bucket.user_session_data.bucket
}

output "firehose_stream_name" {
  value = aws_kinesis_firehose_delivery_stream.user_session_stream.name
}

output "sqs_queue_url" {
  value = aws_sqs_queue.user_session_queue.url
}

output "rds_endpoint" {
  value = aws_rds_cluster.user_session_db.endpoint
} 
# Infrastructure Deployment Guide

This guide provides instructions for deploying the necessary infrastructure for the Spring Boot application using AWS CloudFormation. Our infrastructure includes Amazon ECS on Fargate for container management, Application Load Balancer (ALB) for traffic distribution, and essential networking components like VPC and subnets.

## Prerequisites

Before you begin, ensure you have the following prerequisites:
1. An AWS account with access to CloudFormation, ECS, ECR, and API Gateway.
2. AWS CLI installed and configured on your machine. Installation Guide
3. An existing Docker image for your Spring Boot application pushed to Amazon ECR.

## Deployment Steps

1. **Deploy the CloudFormation Stack**
   
    Deploy the [infrastructure.yaml](infrastructure.yaml) file using AWS CloudFormation
2. **Monitor the Stack Creation** 
   
    Monitor the stack creation process in the AWS CloudFormation console. The process may take several minutes to complete. Ensure that all resources are created successfully.
3. **Accessing the Application**
   
    Once the stack creation is complete, your Spring Boot application will be accessible through the ALB's DNS name.

## Cleanup

To delete the infrastructure and avoid incurring future charges, use the following command:

`aws cloudformation delete-stack --stack-name MySpringBootAppInfrastructure`


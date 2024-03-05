# Github Wrapper

Welcome to the documentation for Application Name! This README provides you with the necessary information to get started with running and using the application, including accessing its Swagger UI documentation and deploying it within a Docker container.

## Getting Started

To get started with this application, clone the repository to your local machine and follow the setup instructions provided below.

## Prerequisites

Before running the application, ensure you have the following installed on your system:

* Java JDK 21
* Docker

## Building the Application

To build the application using Gradle, navigate to the root directory of the project in your terminal and execute the following command:

`gradle build`

This command compiles your application and creates an executable JAR file in the build/libs directory.

## Running the Application Locally

If you wish to run the application locally without Docker, use the following Gradle command:

`gradle bootRun`

This will start the application on your local machine.

## Accessing the Documentation

Once the application is running, you can access the Swagger UI documentation at:

http://localhost:8080/webjars/swagger-ui/index.html#/

This provides an interactive interface to test and explore the API endpoints available within the application.

## Running the Application in a Docker Container

To run your application within a Docker container, follow these steps:

### 1. Build the Docker Image
Run the following command in your terminal, replacing your-application with the name of your application:

`docker build -t github-wrapper .`

### 2. Run the Docker Container
To run your newly created Docker image as a container, execute:

`docker run -p 8080:8080 github-wrapper`

This command runs the application inside a Docker container and makes it accessible on http://localhost:8080.

## Dev environment

http://github-w-alb-acdjdlgq3hyg-1475071239.us-east-1.elb.amazonaws.com

## Infrastructure

All Infrastructure description located here [README.md](aws%2FREADME.md)

## Verification 

Example:

http://github-w-alb-acdjdlgq3hyg-1475071239.us-east-1.elb.amazonaws.com/api/v1/users/{username}/repositories

REPLACE {username} with your github user. 





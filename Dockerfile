# Use an official Java runtime as a parent image
FROM openjdk:21

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from your target directory to the container
COPY build/libs/*.jar /app/application.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the JAR file
CMD ["java", "-jar", "/app/application.jar"]
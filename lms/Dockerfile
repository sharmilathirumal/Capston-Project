# Run this project with java:17
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar file into the container
COPY target/lms-0.0.1-SNAPSHOT.jar lms.jar

# Create uploads directory for file uploads
RUN mkdir -p /app/uploads

# Expose port 8080 to the outside world
EXPOSE 8080

# Command to run the jar file
ENTRYPOINT ["java", "-jar", "lms.jar"]
# docker file configuration
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/lms-0.0.1-SNAPSHOT.jar lms.jar
RUN mkdir -p /app/uploads
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "lms.jar"]
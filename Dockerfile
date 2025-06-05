# admin-service/Dockerfile
FROM gradle:8.5.0-jdk17 AS builder
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . .
RUN gradle build -x test --no-daemon --stacktrace || (echo "Gradle build failed"; exit 1)

FROM openjdk:17-jdk-slim
VOLUME /tmp
ENTRYPOINT ["java", "-jar", "/app.jar"]
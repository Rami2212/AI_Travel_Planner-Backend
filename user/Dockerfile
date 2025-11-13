FROM openjdk:25-jdk-slim

WORKDIR /app

COPY target/user-1.0.0.jar app.jar

ENV SPRING_PROFILES_ACTIVE=docker

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
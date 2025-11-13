FROM openjdk:25-jdk-slim

WORKDIR /app

COPY target/gateway-1.0.0.jar app.jar

ENV SPRING_PROFILES_ACTIVE=docker

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

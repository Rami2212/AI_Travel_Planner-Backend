FROM openjdk:25-jdk-slim

WORKDIR /app

COPY target/itinerary-1.0.0.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
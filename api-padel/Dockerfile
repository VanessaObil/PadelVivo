# Etapa 1: Construir con Maven y Java 21
FROM maven:3.9.8-eclipse-temurin-21 AS build

WORKDIR /app

COPY certs/ certs/
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Etapa 2: Imagen final
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/api-padel-0.0.1-SNAPSHOT.jar app.jar
COPY --from=build /app/certs /app/certs

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

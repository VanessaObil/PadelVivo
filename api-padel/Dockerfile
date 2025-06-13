# Etapa 1: Construir con Maven
FROM maven:3.9.6-jdk-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Etapa 2: Ejecutar con JDK
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/api-padel-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

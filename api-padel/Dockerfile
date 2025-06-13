# Etapa 1: Build con Maven y Java 21
FROM maven:eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn clean package -DskipTests

# Etapa 2: Imagen ligera para ejecutar Spring Boot
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/api-padel-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

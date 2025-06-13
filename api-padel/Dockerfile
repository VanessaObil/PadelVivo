# Etapa 1: Construir con Maven usando Java 21
FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn clean package -DskipTests

# Etapa 2: Imagen final ligera
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/api-padel-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

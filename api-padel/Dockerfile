# Etapa 1: Construir con Maven y Java 21
FROM maven:3.9.8-eclipse-temurin-21 AS build

WORKDIR /app

# Copiar solo lo necesario primero (mejora velocidad de build)
COPY pom.xml .
COPY src ./src
COPY certs ./certs

# Construir el proyecto
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copiar JAR y certificados
COPY --from=build /app/target/api-padel-0.0.1-SNAPSHOT.jar app.jar
COPY --from=build /app/certs /app/certs

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
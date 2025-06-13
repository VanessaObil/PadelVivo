# Etapa 1: Construir con Maven y Java 21
FROM maven:3.9.9-jdk-21 AS build

WORKDIR /app

COPY pom.xml ./
COPY src ./src

# Construir el proyecto
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final ligera para ejecutar el JAR
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copiar el JAR generado
COPY --from=build /app/target/api-padel-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

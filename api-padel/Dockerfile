# Etapa 1: Construir con Maven (usamos una etiqueta válida con Java 21)
FROM maven:3.9.6-jdk-21 AS build

WORKDIR /app

# Copiar archivos del proyecto
COPY pom.xml .
COPY src ./src

# Construir el proyecto sin ejecutar tests
RUN mvn clean package -DskipTests

# Etapa 2: Ejecutar la aplicación con JDK 21
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copiar JAR desde la etapa anterior
COPY --from=build /app/target/api-padel-0.0.1-SNAPSHOT.jar app.jar

# Iniciar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]

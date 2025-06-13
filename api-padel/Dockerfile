# Etapa 1: construir con Maven
FROM maven:3.9.4-jdk-21 AS build

WORKDIR /app

# Copia archivos de pom y código fuente
COPY pom.xml .
COPY src ./src

# Construye el proyecto (sin tests para acelerar)
RUN mvn clean package -DskipTests

# Etapa 2: imagen de ejecución
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copia el JAR generado en la etapa anterior
COPY --from=build /app/target/api-padel-0.0.1-SNAPSHOT.jar app.jar

# Ejecuta la app
ENTRYPOINT ["java", "-jar", "/app.jar"]

# Usamos una imagen base con JDK listo
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copiamos el JAR compilado (ajusta según tu nombre de archivo)
COPY target/api-padel-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto donde corre la app Spring Boot
EXPOSE 8080

# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
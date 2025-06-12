package com.padel.api_padel.mostrar;

import com.mongodb.MongoException;
import com.mongodb.client.*;
import org.bson.Document;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MostrarDatos {

    private static final Logger logger = Logger.getLogger(MostrarDatos.class.getName());

    // Conexión MongoDB
    private static final String MONGO_DB_URL = "mongodb+srv://vane:autografo1@cluster0.z2xz7bp.mongodb.net/PADELVIVO?retryWrites=true&w=majority";
    private static final String MONGO_DB_NAME = "PADELVIVO";

    // Conexión MariaDB
    private static final String MARIA_DB_URL = "jdbc:mariadb://localhost:3308/PadelVivo";
    private static final String MARIA_DB_USER = "root";
    private static final String MARIA_DB_PASSWORD = "rootroot";

    public static void main(String[] args) {
        mostrarDatosEnConsola();
    }

    private static void mostrarDatosEnConsola() {
        logger.log(Level.INFO, "🔍 Iniciando consulta de datos...");

        try (MongoClient mongoClient = MongoClients.create(MONGO_DB_URL)) {
            mostrarDatosPartidosMongoDB(mongoClient);
            mostrarActualizacionesPartidosMongoDB(mongoClient);
        } catch (MongoException e) {
            logger.log(Level.SEVERE, "❌ Error al conectar con MongoDB", e);
        }

        mostrarDatosJugadoresMariaDB();

        logger.log(Level.INFO, "✅ Consulta finalizada.");
    }

    private static void mostrarDatosPartidosMongoDB(MongoClient mongoClient) {
        logger.log(Level.INFO, "📌 Consultando partidos en MongoDB...");
        try {
            MongoDatabase database = mongoClient.getDatabase(MONGO_DB_NAME);
            MongoCollection<Document> collection = database.getCollection("Partidos");

            FindIterable<Document> findIterable = collection.find();
            MongoCursor<Document> cursor = findIterable.iterator();

            int count = 0;
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                count++;
                logger.log(Level.INFO, "📌 Partido encontrado: " + doc.toJson());
            }

            if (count == 0) {
                logger.log(Level.WARNING, "⚠ No se encontraron partidos en la base de datos.");
            }

        } catch (MongoException e) {
            logger.log(Level.SEVERE, "❌ Error al leer datos de partidos", e);
        }
    }

    private static void mostrarActualizacionesPartidosMongoDB(MongoClient mongoClient) {
        logger.log(Level.INFO, "📌 Consultando actualizaciones de partidos en MongoDB...");
        try {
            MongoDatabase database = mongoClient.getDatabase(MONGO_DB_NAME);
            MongoCollection<Document> collection = database.getCollection("Actualizaciones");

            FindIterable<Document> findIterable = collection.find();
            MongoCursor<Document> cursor = findIterable.iterator();

            int count = 0;
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                count++;
                logger.log(Level.INFO, "📌 Actualización encontrada: " + doc.toJson());
            }

            if (count == 0) {
                logger.log(Level.WARNING, "⚠ No se encontraron actualizaciones de partidos.");
            }

        } catch (MongoException e) {
            logger.log(Level.SEVERE, "❌ Error al leer actualizaciones", e);
        }
    }

    private static void mostrarDatosJugadoresMariaDB() {
        logger.log(Level.INFO, "📌 Consultando jugadores en MariaDB...");
        try (Connection conn = DriverManager.getConnection(MARIA_DB_URL, MARIA_DB_USER, MARIA_DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, name, ranking, points, nationality FROM players")) {

            int count = 0;
            while (rs.next()) {
                count++;
                logger.log(Level.INFO, String.format("📌 Jugador encontrado: ID=%d, Nombre=%s, Ranking=%d, Puntos=%d, Nacionalidad=%s",
                        rs.getInt("id"), rs.getString("name"), rs.getInt("ranking"), rs.getInt("points"), rs.getString("nationality")));
            }

            if (count == 0) {
                logger.log(Level.WARNING, "⚠ No se encontraron jugadores en MariaDB.");
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "❌ Error al leer jugadores desde MariaDB", e);
        }
    }
}

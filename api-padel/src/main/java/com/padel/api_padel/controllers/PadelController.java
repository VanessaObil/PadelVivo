/*package com.padel.api_padel.controllers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.padel.api_padel.config.DatabaseConfig;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;  // Importa esta clase
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200") // Permite el acceso desde el frontend
public class PadelController {

    private final DatabaseConfig databaseConfig;

    public PadelController(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    // Obtiene todos los partidos desde MongoDB
    @GetMapping("/partidos")
    public ResponseEntity<String> getPartidos() {
        try (MongoClient mongoClient = MongoClients.create(databaseConfig.getMongoDbUrl())) {
            MongoDatabase database = mongoClient.getDatabase(databaseConfig.getMongoDbName());
            MongoCollection<Document> collection = database.getCollection("Partidos");

            List<JSONObject> partidos = new ArrayList<>();
            for (Document document : collection.find()) {
                JSONObject partido = new JSONObject();
                partido.put("partidoId", document.getString("partidoId"));
                partido.put("marcadorActual", document.getString("marcadorActual"));
                partido.put("estado", document.getString("estado"));
                partido.put("sets", document.getString("sets"));
                partido.put("puntos", document.getString("puntos"));
                partido.put("jugadores", document.get("jugadores"));
                partido.put("fechaCreacion", document.get("fechaCreacion"));
                partidos.add(partido);
            }
            String jsonResponse = new JSONArray(partidos).toString();
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            // Devuelve un error 500 con un mensaje JSON
            String errorMessage = "Error obteniendo partidos: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"" + errorMessage + "\"}");
        }
    }

    // Obtiene todas las actualizaciones desde MongoDB
    @GetMapping("/actualizaciones")
    public ResponseEntity<String> getActualizaciones() {
        try (MongoClient mongoClient = MongoClients.create(databaseConfig.getMongoDbUrl())) {
            MongoDatabase database = mongoClient.getDatabase(databaseConfig.getMongoDbName());
            MongoCollection<Document> collection = database.getCollection("Actualizaciones");

            List<JSONObject> actualizaciones = new ArrayList<>();
            for (Document document : collection.find()) {
                JSONObject actualizacion = new JSONObject();
                actualizacion.put("idActualizacion", document.getString("idActualizacion"));
                actualizacion.put("partidoId", document.getString("partidoId"));
                actualizacion.put("tipo", document.getString("tipo"));
                actualizacion.put("datos", document.getString("datos"));
                actualizacion.put("fecha", document.get("fecha"));
                actualizaciones.add(actualizacion);
            }
            String jsonResponse = new JSONArray(actualizaciones).toString();
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            // Devuelve un error 500 con un mensaje JSON
            String errorMessage = "Error obteniendo actualizaciones: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"" + errorMessage + "\"}");
        }
    }

    // Obtiene todos los jugadores desde MariaDB
    @GetMapping("/jugadores")
    public ResponseEntity<String> getJugadores() {
        try (Connection conn = DriverManager.getConnection(databaseConfig.getMariaDbUrl(), databaseConfig.getMariaDbUser(), databaseConfig.getMariaDbPassword());
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, name, ranking, points, nationality FROM Players")) {

            List<JSONObject> jugadores = new ArrayList<>();
            while (rs.next()) {
                JSONObject jugador = new JSONObject();
                jugador.put("id", rs.getInt("id"));
                jugador.put("name", rs.getString("name"));
                jugador.put("ranking", rs.getInt("ranking"));
                jugador.put("points", rs.getInt("points"));
                jugador.put("nationality", rs.getString("nationality"));
                jugadores.add(jugador);
            }
            String jsonResponse = new JSONArray(jugadores).toString();
            return ResponseEntity.ok(jsonResponse);
        } catch (SQLException e) {
            // Devuelve un error 500 con un mensaje JSON
            String errorMessage = "Error obteniendo jugadores: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"" + errorMessage + "\"}");

        }
    }
}*/
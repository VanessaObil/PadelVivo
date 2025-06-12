package com.padel.api_padel.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.padel.api_padel.client.ApiClient;
import com.padel.api_padel.database.DatabaseManager;
import jakarta.annotation.PostConstruct;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Clase de servicio para la inicialización de datos en la base de datos.
 * Esta clase se encarga de cargar datos iniciales sobre jugadores, partidos, torneos y temporadas.
 * Utiliza la API externa para obtener la información y luego la guarda en bases de datos tanto SQL como NoSQL.
 */
@Service
public class Iniciar {

    // Dependencias inyectadas
    private final ApiClient apiClient;  // Cliente para hacer peticiones a la API externa
    private final DatabaseManager databaseManager;  // Administrador de la base de datos (interacción con MariaDB y MongoDB)
    private final MongoDatabase mongoDatabase;  // Base de datos MongoDB para almacenar partidos

    @Autowired
    private JdbcTemplate jdbcTemplate;  // Plantilla JDBC para realizar operaciones en MariaDB

    /**
     * Constructor de la clase.
     *
     * @param apiClient Cliente para hacer peticiones a la API externa
     * @param databaseManager Administrador de base de datos
     * @param mongoDatabase Base de datos MongoDB para guardar los partidos
     */
    @Autowired
    public Iniciar(ApiClient apiClient, DatabaseManager databaseManager, MongoDatabase mongoDatabase) {
        this.apiClient = apiClient;
        this.databaseManager = databaseManager;
        this.mongoDatabase = mongoDatabase;
    }

    /**
     * Método que se ejecuta después de la construcción del objeto para cargar los datos iniciales.
     * Este método llama a los métodos para cargar jugadores, partidos, torneos y temporadas.
     */
    @PostConstruct
    public void loadInitialData() {
        // Descomentar las líneas necesarias según los datos que se deseen cargar
        //fetchAndStorePlayers();
        //fetchAndStoreMatches();  // Carga los partidos
        //fetchAndStoreTournaments();
        //fetchAndStoreSeasons();
    }

    /**
     * Método que obtiene y guarda los jugadores desde la API externa.
     * Se realiza una petición por cada página de jugadores y se guardan los datos en la base de datos MariaDB.
     */
    public void fetchAndStorePlayers() {
        String endpoint = "/players";
        String nextPageUrl = endpoint;
        String previousPageUrl = "";
        String baseUrl = apiClient.getBaseUrl();

        // Ciclo para recorrer todas las páginas de jugadores
        while (nextPageUrl != null && !nextPageUrl.isEmpty()) {
            try {
                if (nextPageUrl.equals(previousPageUrl)) {
                    System.out.println("La siguiente página es igual a la anterior. Deteniendo para evitar bucle infinito.");
                    break;
                }
                previousPageUrl = nextPageUrl;

                // Construcción de la URL completa para la llamada a la API
                String fullUrl = nextPageUrl.startsWith("http") ? nextPageUrl : baseUrl + nextPageUrl;
                System.out.println("Llamando a la API: " + fullUrl);

                String response = apiClient.callApi(fullUrl);
                JSONObject jsonResponse = new JSONObject(response);

                // Procesar los datos de los jugadores
                if (jsonResponse.has("data")) {
                    JSONArray dataArray = jsonResponse.getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject jugador = dataArray.getJSONObject(i);
                        System.out.println("Jugador: " + jugador.toString());
                        try {
                            // Guardar los datos del jugador en la base de datos
                            databaseManager.guardarDatosJugadorEnMariaDB(jugador);
                        } catch (SQLException e) {
                            System.err.println("Error al guardar jugador en MariaDB: " + e.getMessage());
                        }
                    }
                }

                // Comprobar si existe un enlace a la siguiente página de jugadores
                JSONObject links = jsonResponse.optJSONObject("links");
                if (links != null && links.has("next")) {
                    nextPageUrl = links.optString("next", null);
                    if (nextPageUrl == null || nextPageUrl.equals("null") || nextPageUrl.isEmpty()) {
                        System.out.println("No hay más páginas.");
                        break;
                    }

                    // Espera antes de continuar con la siguiente página
                    System.out.println("⌛ Esperando 1 segundo antes de continuar con la siguiente página...");
                    Thread.sleep(1000);
                } else {
                    System.out.println("No se encontró el campo 'next'. Terminando.");
                    break;
                }

            } catch (IOException | InterruptedException e) {
                System.err.println("Error al llamar a la API: " + e.getMessage());
                break;
            }
        }
    }

    /**
     * Método que obtiene y guarda los partidos desde la API externa.
     * Los partidos son procesados y almacenados en la base de datos MongoDB.
     */
    public void fetchAndStoreMatches() {
        String endpoint = "/matches";
        String nextPageUrl = endpoint;
        String baseUrl = apiClient.getBaseUrl();

        try {
            while (nextPageUrl != null && !nextPageUrl.isEmpty()) {
                if (baseUrl.endsWith("/")) {
                    baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
                }

                // Construcción de la URL completa para la llamada a la API
                String fullUrl = nextPageUrl.startsWith("http") ? nextPageUrl : baseUrl + nextPageUrl;
                System.out.println("Llamando a la API: " + fullUrl);

                String response = apiClient.callApi(fullUrl);
                JSONObject jsonResponse = new JSONObject(response);

                // Procesar los partidos
                if (jsonResponse.has("data")) {
                    JSONArray matches = jsonResponse.getJSONArray("data");

                    for (int i = 0; i < matches.length(); i++) {
                        JSONObject partido = matches.getJSONObject(i);
                        String partidoId = partido.optString("id", "sin_id");

                        // Verificar si el partido ya existe en MongoDB
                        MongoCollection<Document> collection = mongoDatabase.getCollection("Partidos");
                        Document partidoExistente = collection.find(new Document("partidoId", partidoId)).first();
                        if (partidoExistente != null) {
                            System.out.println("⚠️ Partido con ID " + partidoId + " ya existe. Se omite.");
                            continue;
                        }

                        // Obtener marcador
                        String marcadorActualStr = partido.optString("score", "[]");
                        JSONArray marcadorActual = new JSONArray(marcadorActualStr);
                        int numberOfSets = marcadorActual.length();
                        StringBuilder marcador = new StringBuilder();
                        for (int j = 0; j < numberOfSets; j++) {
                            JSONObject set = marcadorActual.getJSONObject(j);
                            int team1Score = set.optInt("team_1", 0);
                            int team2Score = set.optInt("team_2", 0);
                            marcador.append(team1Score).append("-").append(team2Score);
                            if (j < numberOfSets - 1) {
                                marcador.append(", ");
                            }
                        }

                        String estado = partido.optString("status", "Pendiente");
                        String puntos = partido.optString("points", "0");

                        String[] jugadores = new String[0];
                        String[] nacionalidades = new String[0];

                        try {
                            JSONObject connections = partido.optJSONObject("connections");
                            if (connections != null && connections.has("players")) {
                                String playersUrl = connections.optString("players");
                                String fullPlayersUrl = playersUrl.startsWith("http")
                                        ? playersUrl
                                        : baseUrl + playersUrl.replaceFirst("^/api/", "/");

                                System.out.println("Obteniendo jugadores desde: " + fullPlayersUrl);
                                String playersResponse = apiClient.callApi(fullPlayersUrl);
                                JSONObject playersJson = new JSONObject(playersResponse);

                                JSONArray playersArray = playersJson.optJSONArray("data");
                                if (playersArray != null) {
                                    jugadores = new String[playersArray.length()];
                                    nacionalidades = new String[playersArray.length()];

                                    for (int j = 0; j < playersArray.length(); j++) {
                                        JSONObject player = playersArray.getJSONObject(j);
                                        String playerId = player.optString("id");
                                        String nombreJugador = player.optString("name", "Jugador desconocido");

                                        String playerDetailsUrl = "/players/" + playerId;
                                        String playerDetailsResponse = apiClient.callApi(baseUrl + playerDetailsUrl);
                                        JSONObject playerDetailsJson = new JSONObject(playerDetailsResponse);
                                        String nacionalidadJugador = playerDetailsJson.optString("nationality", "Desconocida");

                                        jugadores[j] = nombreJugador;
                                        nacionalidades[j] = nacionalidadJugador;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            System.err.println("❌ Error al obtener jugadores para el partido " + partidoId + ": " + e.getMessage());
                        }

                        Date fechaPartido = null;
                        String fechaPartidoString = partido.optString("played_at", null);
                        if (fechaPartidoString != null && !fechaPartidoString.isEmpty()) {
                            try {
                                fechaPartido = java.sql.Date.valueOf(fechaPartidoString);
                            } catch (Exception e) {
                                System.err.println("❌ Error al convertir la fecha del partido: " + e.getMessage());
                            }
                        }

                        if (jugadores.length > 0) {
                            databaseManager.guardarDatosPartidoEnMongoDB(partidoId, marcador.toString(), estado,
                                    String.valueOf(numberOfSets), puntos,
                                    jugadores, nacionalidades, fechaPartido);
                            System.out.println("✅ Partido guardado en MongoDB con jugadores y nacionalidades.");
                        } else {
                            System.out.println("⚠️ Partido " + partidoId + " no tiene jugadores. No se guarda.");
                        }
                    }
                }

                // Comprobar si existe un enlace a la siguiente página de partidos
                JSONObject links = jsonResponse.optJSONObject("links");
                if (links != null && links.has("next")) {
                    nextPageUrl = links.optString("next", null);
                    if (nextPageUrl == null || nextPageUrl.equals("null") || nextPageUrl.isEmpty()) break;
                    Thread.sleep(4000); // Esperar antes de la siguiente página
                } else {
                    break;
                }
            }

            System.out.println("✅ Partidos procesados correctamente.");
        } catch (Exception e) {
            System.err.println("❌ Error general al guardar partidos: " + e.getMessage());
        }
    }

    /**
     * Método que obtiene y guarda los torneos desde la API externa.
     * Los torneos son procesados y almacenados en la base de datos MariaDB.
     */
    public void fetchAndStoreTournaments() {
        String endpoint = "/tournaments";
        String baseUrl = apiClient.getBaseUrl();
        String nextPageUrl = endpoint;

        try {
            while (nextPageUrl != null && !nextPageUrl.isEmpty()) {
                String fullUrl = nextPageUrl.startsWith("http") ? nextPageUrl : baseUrl + nextPageUrl;

                System.out.println("Llamando a la API para obtener torneos: " + fullUrl);
                String response = apiClient.callApi(fullUrl);
                JSONObject jsonResponse = new JSONObject(response);

                // Procesar los torneos
                if (jsonResponse.has("data")) {
                    JSONArray dataArray = jsonResponse.getJSONArray("data");

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject torneo = dataArray.getJSONObject(i);
                        System.out.println("Torneo obtenido: " + torneo.toString());
                        try {
                            // Guardar los datos del torneo en la base de datos MariaDB
                            databaseManager.guardarDatosTorneoEnMariaDB(torneo);
                        } catch (SQLException e) {
                            System.err.println("Error al guardar torneo en la base de datos: " + e.getMessage());
                        }
                    }
                }

                // Comprobar si existe un enlace a la siguiente página de torneos
                JSONObject links = jsonResponse.optJSONObject("links");
                nextPageUrl = (links != null) ? links.optString("next", null) : null;

                if (nextPageUrl == null || "null".equals(nextPageUrl)) {
                    break;
                }

                Thread.sleep(1000);
            }

            System.out.println("✅ Torneos procesados correctamente.");
        } catch (Exception e) {
            System.err.println("❌ Error al procesar los torneos: " + e.getMessage());
        }
    }

    /**
     * Método para validar las fechas de un torneo.
     *
     * @param torneo Objeto JSON con los datos del torneo
     * @return Verdadero si las fechas son válidas, falso en caso contrario
     */
    public boolean validarFechasTorneo(JSONObject torneo) {
        try {
            String startDateStr = torneo.getString("start_date");
            String endDateStr = torneo.getString("end_date");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);

            return !startDate.after(endDate);  // La fecha de inicio no puede ser después de la fecha de fin
        } catch (ParseException e) {
            System.err.println("Error al validar las fechas del torneo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método para validar las fechas de una temporada.
     *
     * @param temporada Objeto JSON con los datos de la temporada
     * @return Verdadero si las fechas son válidas, falso en caso contrario
     */
    public boolean validarFechasTemporada(JSONObject temporada) {
        try {
            String startDateStr = temporada.getString("start_date");
            String endDateStr = temporada.getString("end_date");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);

            return !startDate.after(endDate);  // La fecha de inicio no puede ser después de la fecha de fin
        } catch (ParseException e) {
            System.err.println("Error al validar las fechas de la temporada: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método que obtiene y guarda las temporadas desde la API externa.
     * Las temporadas son procesadas y almacenadas en la base de datos MariaDB.
     */
    public void fetchAndStoreSeasons() {
        String endpoint = "/seasons";
        String nextPageUrl = endpoint;
        String baseUrl = apiClient.getBaseUrl();

        try {
            while (nextPageUrl != null && !nextPageUrl.isEmpty()) {
                String fullUrl = nextPageUrl.startsWith("http") ? nextPageUrl : baseUrl + nextPageUrl;
                String response = apiClient.callApi(fullUrl);
                JSONObject jsonResponse = new JSONObject(response);

                // Procesar las temporadas
                if (jsonResponse.has("data")) {
                    JSONArray data = jsonResponse.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject temporada = data.getJSONObject(i);

                        if (validarFechasTemporada(temporada)) {
                            // Guardar los datos de la temporada en la base de datos MariaDB
                            databaseManager.guardarDatosTemporadaEnMariaDB(temporada);
                        }
                    }
                }

                // Comprobar si existe un enlace a la siguiente página de temporadas
                JSONObject links = jsonResponse.optJSONObject("links");
                nextPageUrl = (links != null) ? links.optString("next", null) : null;
                if (nextPageUrl == null || "null".equals(nextPageUrl)) break;
                Thread.sleep(1000);
            }

            System.out.println("Temporadas procesadas correctamente.");
        } catch (Exception e) {
            System.err.println("Error al procesar temporadas: " + e.getMessage());
        }
    }
}

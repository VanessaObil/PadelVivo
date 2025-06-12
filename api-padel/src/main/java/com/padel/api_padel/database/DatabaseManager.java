package com.padel.api_padel.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import static com.mongodb.client.model.Filters.in;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Clase que gestiona la interacción con las bases de datos MariaDB y MongoDB.
 * Realiza operaciones de inserción, actualización y verificación de datos en ambas bases de datos.
 */
@Component
public class DatabaseManager {

    private final JdbcTemplate jdbcTemplate;
    private final MongoDatabase mongoDatabase;

    /**
     * Constructor para inicializar las dependencias necesarias.
     *
     * @param jdbcTemplate plantilla de JDBC para interactuar con MariaDB.
     * @param mongoDatabase base de datos MongoDB para interactuar con colecciones Mongo.
     */
    @Autowired
    public DatabaseManager(JdbcTemplate jdbcTemplate, MongoDatabase mongoDatabase) {
        this.jdbcTemplate = jdbcTemplate;
        this.mongoDatabase = mongoDatabase;
    }

    /**
     * Guarda los datos de un jugador en MariaDB.
     * Si el jugador no existe, inserta los datos. Si existe, los actualiza.
     *
     * @param jugador JSON que contiene los datos del jugador.
     * @throws SQLException Si ocurre un error en la ejecución de la consulta SQL.
     */
    public void guardarDatosJugadorEnMariaDB(JSONObject jugador) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM Players WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(checkQuery, Integer.class, jugador.getInt("id"));

        if (count == 0) {
            // Si el jugador no existe, realizar la inserción
            String insertQuery = "INSERT INTO Players (id, name, url, ranking, points, height, nationality, birthplace, birthdate, market_value, hand, side) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(insertQuery,
                    jugador.getInt("id"),
                    jugador.getString("name"),
                    jugador.isNull("url") ? null : jugador.getString("url"),
                    jugador.isNull("ranking") ? null : jugador.getInt("ranking"),
                    jugador.isNull("points") ? null : jugador.getInt("points"),
                    jugador.isNull("height") ? null : jugador.getInt("height"),
                    jugador.getString("nationality"),
                    jugador.isNull("birthplace") ? null : jugador.getString("birthplace"),
                    null, // birthdate - manejar conversión de tipo si es necesario
                    jugador.isNull("market_value") ? null : jugador.getString("market_value"),
                    jugador.isNull("hand") ? null : jugador.getString("hand"),
                    jugador.isNull("side") ? null : jugador.getString("side")
            );

        } else {
            // Si el jugador ya existe, realizar la actualización
            String updateQuery = "UPDATE Players SET name = ?, url = ?, ranking = ?, points = ?, height = ?, nationality = ?, birthplace = ?, birthdate = ?, market_value = ?, hand = ?, side = ? WHERE id = ?";
            jdbcTemplate.update(updateQuery,
                    jugador.getString("name"),
                    jugador.isNull("url") ? null : jugador.getString("url"),
                    jugador.isNull("ranking") ? null : jugador.getInt("ranking"),
                    jugador.isNull("points") ? null : jugador.getInt("points"),
                    jugador.isNull("height") ? null : jugador.getInt("height"),
                    jugador.getString("nationality"),
                    jugador.isNull("birthplace") ? null : jugador.getString("birthplace"),
                    null, // birthdate - manejar conversión de tipo si es necesario
                    jugador.isNull("market_value") ? null : jugador.getString("market_value"),
                    jugador.isNull("hand") ? null : jugador.getString("hand"),
                    jugador.isNull("side") ? null : jugador.getString("side"),
                    jugador.getInt("id")
            );
        }
    }

    /**
     * Guarda los datos de un partido en MongoDB.
     * Si el partido ya existe, no realiza ninguna acción. Si no existe, inserta los datos.
     *
     * @param partidoId       Identificador del partido.
     * @param marcadorActual  Marcador actual del partido.
     * @param estadoPartido   Estado del partido (por ejemplo, en curso, terminado).
     * @param setsActuales    Sets actuales del partido.
     * @param puntosActuales  Puntos actuales del partido.
     * @param jugadores       Lista de jugadores del partido.
     * @param nacionalidades  Lista de nacionalidades de los jugadores.
     * @param fechaPartido    Fecha del partido.
     */
    public void guardarDatosPartidoEnMongoDB(String partidoId, String marcadorActual, String estadoPartido,
                                             String setsActuales, String puntosActuales,
                                             String[] jugadores, String[] nacionalidades, Date fechaPartido) {

        MongoCollection<Document> collection = mongoDatabase.getCollection("Partidos");

        // Verificar si el partido ya existe en la base de datos
        Document partidoExistente = collection.find(new Document("partidoId", partidoId)).first();

        if (partidoExistente == null) {
            // Si el partido no existe, realizar la inserción
            Document partido = new Document("partidoId", partidoId)
                    .append("marcadorActual", marcadorActual)
                    .append("estado", estadoPartido)
                    .append("sets", setsActuales)
                    .append("puntos", puntosActuales)
                    .append("jugadores", Arrays.asList(jugadores))
                    .append("nacionalidades", Arrays.asList(nacionalidades))
                    .append("fechaPartido", fechaPartido);

            collection.insertOne(partido);
            System.out.println("✅ Partido guardado en MongoDB.");
        } else {
            // Si el partido ya existe, no hacer nada
            System.out.println("⚠️ Partido con partidoId " + partidoId + " ya existe. No se guardó.");
        }
    }

    /**
     * Obtiene todos los jugadores de MariaDB.
     *
     * @return Lista de mapas que representan a los jugadores.
     */
    public java.util.List<java.util.Map<String, Object>> obtenerTodosLosJugadores() {
        String sql = "SELECT id, name, ranking, points, nationality FROM Players";
        return jdbcTemplate.queryForList(sql);
    }

    /**
     * Obtiene todos los partidos de MongoDB.
     *
     * @return Lista de documentos que representan los partidos.
     */
    public java.util.List<Document> obtenerTodosLosPartidos() {
        MongoCollection<Document> collection = mongoDatabase.getCollection("Partidos");
        return collection.find().into(new java.util.ArrayList<>());
    }

    public boolean actualizarPartidoEnMongoDB(String partidoId, String marcadorActual, String estadoPartido,
                                              String setsActuales, String puntosActuales,
                                              String[] jugadores, String[] nacionalidades, Date fechaPartido) {

        MongoCollection<Document> collection = mongoDatabase.getCollection("Partidos");

        Document query = new Document("partidoId", partidoId);
        Document partidoExistente = collection.find(query).first();

        if (partidoExistente != null) {
            Document updateFields = new Document()
                    .append("marcadorActual", marcadorActual)
                    .append("estado", estadoPartido)
                    .append("sets", setsActuales)
                    .append("puntos", puntosActuales)
                    .append("jugadores", Arrays.asList(jugadores))  // Convertir jugadores a una lista de MongoDB
                    .append("nacionalidades", Arrays.asList(nacionalidades))
                    .append("fechaPartido", fechaPartido);

            Document updateOperation = new Document("$set", updateFields);

            collection.updateOne(query, updateOperation);
            System.out.println("✅ Partido actualizado correctamente.");
            return true;
        } else {
            System.err.println("❌ Partido no encontrado con ID: " + partidoId);
            return false;
        }
    }






    /**
     * Obtiene todas las actualizaciones de MongoDB.
     *
     * @return Lista de documentos que representan las actualizaciones.
     */
    public java.util.List<Document> obtenerTodasLasActualizaciones() {
        MongoCollection<Document> collection = mongoDatabase.getCollection("Actualizaciones");
        return collection.find().into(new java.util.ArrayList<>());
    }

    /**
     * Obtiene el nombre de un jugador desde MariaDB utilizando su ID.
     *
     * @param jugadorId ID del jugador.
     * @return Nombre del jugador o null si no se encuentra.
     */
    public String obtenerNombreJugadorDesdeMariaDB(int jugadorId) {
        try {
            String sql = "SELECT name FROM Players WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, String.class, jugadorId);
        } catch (Exception e) {
            System.err.println("Jugador con ID " + jugadorId + " no encontrado.");
            return null;
        }
    }

    /**
     * Verifica si un partido existe en MongoDB.
     *
     * @param partidoId Identificador del partido.
     * @return true si el partido existe, false si no existe.
     */
    public boolean partidoExisteEnMongoDB(String partidoId) {
        MongoCollection<Document> collection = mongoDatabase.getCollection("Partidos");
        Document query = new Document("partidoId", partidoId);
        return collection.find(query).first() != null;
    }

    /**
     * Guarda los datos de un torneo en MariaDB.
     * Si el torneo ya existe, actualiza sus datos. Si no existe, lo inserta.
     *
     * @param torneo JSON que contiene los datos del torneo.
     * @throws SQLException Si ocurre un error en la ejecución de la consulta SQL.
     */
    public void guardarDatosTorneoEnMariaDB(JSONObject torneo) throws SQLException {

        String checkQuery = "SELECT COUNT(*) FROM Tournaments WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(checkQuery, Integer.class, torneo.getInt("id"));

        String name = torneo.getString("name");
        String location = torneo.getString("location");
        String country = torneo.getString("country");
        String level = torneo.getString("level");
        String status = torneo.getString("status");
        String startDate = torneo.getString("start_date");
        String endDate = torneo.getString("end_date");

        Integer seasonId = null;
        if (torneo.has("connections")) {
            JSONObject connections = torneo.getJSONObject("connections");
            String seasonPath = connections.has("season") ? connections.getString("season") : null;
            if (seasonPath != null) {
                seasonId = Integer.parseInt(seasonPath.replaceAll("\\D+", ""));
            }
        }

        if (count == 0) {
            // Si el torneo no existe, realizar la inserción
            String insertQuery = "INSERT INTO Tournaments (id, name, location, country, level, status, start_date, end_date, season_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(insertQuery,
                    torneo.getInt("id"),
                    name,
                    location,
                    country,
                    level,
                    status,
                    startDate,
                    endDate,
                    seasonId
            );
            System.out.println("✅ Torneo insertado correctamente en la base de datos.");
        } else {
            // Si el torneo ya existe, realizar la actualización
            String updateQuery = "UPDATE Tournaments SET name = ?, location = ?, country = ?, level = ?, status = ?, " +
                    "start_date = ?, end_date = ?, season_id = ? WHERE id = ?";
            jdbcTemplate.update(updateQuery,
                    name,
                    location,
                    country,
                    level,
                    status,
                    startDate,
                    endDate,
                    seasonId,
                    torneo.getInt("id")
            );
            System.out.println("✅ Torneo actualizado correctamente en la base de datos.");
        }
    }

    /**
     * Guarda los datos de una temporada en MariaDB.
     * Si la temporada no existe, la inserta. Si ya existe, la actualiza.
     *
     * @param temporada JSON que contiene los datos de la temporada.
     * @throws SQLException Si ocurre un error en la ejecución de la consulta SQL.
     */
    public void guardarDatosTemporadaEnMariaDB(JSONObject temporada) throws SQLException {
        // Verificar si la temporada ya existe
        String checkQuery = "SELECT COUNT(*) FROM Seasons WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(checkQuery, Integer.class, temporada.getInt("id"));

        // Obtener y validar fechas
        String startDate = temporada.getString("start_date");  // Asegúrate de que el formato sea "yyyy-MM-dd"
        String endDate = temporada.getString("end_date");

        if (count == 0) {
            // Si no existe, realizar la inserción
            String insertQuery = "INSERT INTO Seasons (id, name, start_date, end_date) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(insertQuery,
                    temporada.getInt("id"),
                    temporada.getString("name"),
                    startDate,
                    endDate
            );
        } else {
            // Si ya existe, realizar la actualización
            String updateQuery = "UPDATE Seasons SET name = ?, start_date = ?, end_date = ? WHERE id = ?";
            jdbcTemplate.update(updateQuery,
                    temporada.getString("name"),
                    startDate,
                    endDate,
                    temporada.getInt("id")
            );
        }
    }

    /**
     * Verifica si un torneo existe en MariaDB.
     *
     * @param torneoId ID del torneo a verificar.
     * @return true si el torneo existe, false si no.
     */
    public boolean torneoExisteEnMariaDB(int torneoId) {
        String query = "SELECT COUNT(*) FROM Tournaments WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, torneoId);
        return count > 0;
    }

    /**
     * Obtiene todos los torneos desde MariaDB.
     * Este método consulta la base de datos MariaDB y obtiene una lista de todos los torneos almacenados.
     *
     * @return Lista de mapas, donde cada mapa representa un torneo con sus atributos (id, nombre, fecha_inicio, fecha_fin, lugar).
     */
    public java.util.List<java.util.Map<String, Object>> obtenerTodosLosTorneos() {
        // Consulta SQL que selecciona todos los torneos en la tabla "Tournaments"
        String sql = "SELECT id, nombre, fecha_inicio, fecha_fin, lugar FROM Tournaments";
        // Ejecutar la consulta y retornar el resultado como una lista de mapas.
        return jdbcTemplate.queryForList(sql);
    }

    /**
     * Obtiene todas las temporadas desde MongoDB.
     * Este método consulta la base de datos MongoDB para obtener todos los documentos de la colección "Temporadas".
     *
     * @return Lista de documentos, donde cada documento representa una temporada.
     */
    public java.util.List<Document> obtenerTodasLasTemporadas() {
        // Obtener la colección "Temporadas" de la base de datos MongoDB
        MongoCollection<Document> collection = mongoDatabase.getCollection("Temporadas");
        // Realizar la consulta para obtener todos los documentos de la colección y devolverlos en una lista.
        return collection.find().into(new java.util.ArrayList<>());
    }
    public List<Document> obtenerPartidosPorIds(List<String> matchIds) {
        if (matchIds == null || matchIds.isEmpty()) {
            return new ArrayList<>();
        }
        MongoCollection<Document> collection = mongoDatabase.getCollection("Partidos");

        // Filtro: partidoId IN matchIds
        List<Document> partidos = collection.find(in("partidoId", matchIds)).into(new ArrayList<>());
        return partidos;
    }
}

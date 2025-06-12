/*package com.padel.api_padel.api;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class ApiConnector {

    private static final String MARIA_DB_URL = "jdbc:mariadb://localhost:3308/PadelVivo";
    private static final String MARIA_DB_USER = "root";
    private static final String MARIA_DB_PASSWORD = "rootroot";
    private static final String MONGO_DB_URL = "mongodb+srv://vane:autografo1@cluster0.z2xz7bp.mongodb.net/PADELVIVO?retryWrites=true&w=majority";
    private static final String MONGO_DB_NAME = "PADELVIVO";


    private static final int SERVER_PORT = 8080; // Puedes cambiar el puerto si es necesario

    private static final Map<String, String> ADMIN_CREDENTIALS = new HashMap<>();
    private static final Map<String, String> USER_CREDENTIALS = new HashMap<>(); // *** Declaración e inicialización ***

    static {
        ADMIN_CREDENTIALS.put("admin", "password");
        USER_CREDENTIALS.put("user", "password"); // *** Inicialización de la contraseña del usuario ***
    }

    public static void main(String[] args) {
        ApiConnector connector = new ApiConnector();
        connector.runApiLogic();
    }

    public void runApiLogic() {
        // No es necesario autenticar al administrador aquí para iniciar el servidor
        System.out.println("Iniciando servidor...");
        iniciarServidor();
        // La lógica de la API se puede ejecutar en un hilo separado si es necesario
        // new Thread(this::ejecutarLogicaApi).start();
    }



    private void iniciarServidor() {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Servidor iniciado en el puerto " + SERVER_PORT + ". Esperando conexiones del frontend...");
            while (true) {
                Socket clientSocket = serverSocket.accept(); // Espera a que un cliente se conecte
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());
                // Aquí es donde procesarías la petición del cliente
                new Thread(() -> procesarPeticionCliente(clientSocket)).start(); // Manejar cada cliente en un hilo separado
            }
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }

    private void procesarPeticionCliente(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String requestLine = in.readLine();
            System.out.println("Petición recibida: " + requestLine);

            if (requestLine != null) {
                if (requestLine.startsWith("OPTIONS ")) {
                    // Handle preflight request
                    out.println("HTTP/1.1 200 OK");
                    out.println("Access-Control-Allow-Origin: http://localhost:4200"); // Or "*" for any origin
                    out.println("Access-Control-Allow-Methods: POST, GET, OPTIONS, PUT, DELETE");
                    out.println("Access-Control-Allow-Headers: Content-Type, Authorization");
                    out.println("Access-Control-Max-Age: 3600"); // How long the results of a preflight request can be cached
                    out.println();
                    out.flush();
                    clientSocket.close();
                    System.out.println("Respuesta OPTIONS enviada. Conexión cerrada.");
                    return;
                } else if (requestLine.startsWith("POST /login")) {
                    // ... (your existing POST /login handling code) ...
                    String contentLengthHeader = null;
                    String line;
                    while ((line = in.readLine()) != null && !line.isEmpty()) {
                        if (line.startsWith("Content-Length:")) {
                            contentLengthHeader = line;
                        }
                    }

                    if (contentLengthHeader != null) {
                        int contentLength = Integer.parseInt(contentLengthHeader.substring(contentLengthHeader.indexOf(':') + 1).trim());
                        StringBuilder requestBody = new StringBuilder();
                        for (int i = 0; i < contentLength; i++) {
                            requestBody.append((char) in.read());
                        }
                        String body = requestBody.toString();
                        System.out.println("Cuerpo de la petición de login: " + body);

                        try {
                            JSONObject credentials = new JSONObject(body);
                            String username = credentials.optString("username");
                            String password = credentials.optString("password");

                            String userRole = null;
                            if (ADMIN_CREDENTIALS.containsKey(username) && ADMIN_CREDENTIALS.get(username).equals(password)) {
                                userRole = "admin";
                            } else if (USER_CREDENTIALS.containsKey(username) && USER_CREDENTIALS.get(username).equals(password)) {
                                userRole = "user";
                            }

                            if (userRole != null) {
                                out.println("HTTP/1.1 200 OK");
                                out.println("Content-Type: application/json; charset=UTF-8");
                                out.println("Access-Control-Allow-Origin: http://localhost:4200"); // Add CORS header
                                out.println();
                                out.println("{\"role\":\"" + userRole + "\"}"); // Envía el rol en la respuesta
                            } else {
                                out.println("HTTP/1.1 401 Unauthorized");
                                out.println("Content-Type: application/json; charset=UTF-8");
                                out.println("Access-Control-Allow-Origin: http://localhost:4200"); // Add CORS header
                                out.println();
                                out.println("{\"error\":\"Credenciales inválidas\"}");
                            }
                        } catch (Exception e) {
                            System.err.println("Error al procesar el JSON de login: " + e.getMessage());
                            out.println("HTTP/1.1 400 Bad Request");
                            out.println("Content-Type: application/json; charset=UTF-8");
                            out.println("Access-Control-Allow-Origin: http://localhost:4200"); // Add CORS header
                            out.println();
                            out.println("{\"error\":\"Error en el formato de la petición de login\"}");
                        } finally {
                            out.flush();
                            clientSocket.close();
                            System.out.println("Conexión con el cliente cerrada.");
                            return;
                        }

                    } else {
                        out.println("HTTP/1.1 400 Bad Request");
                        out.println("Content-Type: application/json; charset=UTF-8");
                        out.println("Access-Control-Allow-Origin: http://localhost:4200"); // Add CORS header
                        out.println();
                        out.println("{\"error\":\"Falta la cabecera Content-Length\"}");
                        out.flush();
                        clientSocket.close();
                        System.out.println("Conexión con el cliente cerrada.");
                        return;
                    }
                } else if (requestLine.startsWith("GET ")) {
                    String requestedPath = requestLine.split(" ")[1];
                    String response = "{\"message\":\"Bienvenido al servidor de Padel Vivo!\"}"; // Respuesta por defecto

                    if (requestedPath.equals("/admin/partidos")) {
                        response = obtenerDatosPartidos();
                    } else if (requestedPath.equals("/admin/jugadores")) {
                        response = obtenerDatosJugadores();
                    } else if (requestedPath.equals("/admin/actualizaciones")) {
                        response = obtenerDatosActualizaciones();
                    } else if (requestedPath.equals("/user")) {
                        response = "{\"message\":\"Zona de usuario (aún no implementada)\"}";
                    }

                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-Type: application/json; charset=UTF-8");
                    out.println("Access-Control-Allow-Origin: http://localhost:4200"); // Add CORS header
                    out.println("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
                    out.println("Access-Control-Allow-Headers: Content-Type, Authorization");
                    out.println();
                    out.println(response);
                    out.flush();
                    clientSocket.close();
                    System.out.println("Conexión con el cliente cerrada.");
                    return;
                }
            }

            // Si la petición no coincide con ninguna ruta conocida
            out.println("HTTP/1.1 404 Not Found");
            out.println("Content-Type: application/json; charset=UTF-8");
            out.println("Access-Control-Allow-Origin: http://localhost:4200"); // Add CORS header
            out.println();
            out.println("{\"error\":\"Ruta no encontrada\"}");
            out.flush();
            clientSocket.close();
            System.out.println("Conexión con el cliente cerrada.");

        } catch (IOException e) {
            System.err.println("Error al procesar la petición del cliente: " + e.getMessage());
        }
    }

    private String obtenerDatosJugadores() {
        StringBuilder response = new StringBuilder();
        response.append("{\"jugadores\":[");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(MARIA_DB_URL, MARIA_DB_USER, MARIA_DB_PASSWORD);
            String query = "SELECT id, name, ranking, points, nationality FROM Players"; // Consulta actualizada
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                response.append("{");
                response.append("\"id\":").append(resultSet.getInt("id")).append(",");
                response.append("\"name\":\"").append(resultSet.getString("name")).append("\",");
                response.append("\"ranking\":").append(resultSet.getInt("ranking")).append(",");
                response.append("\"points\":").append(resultSet.getInt("points")).append(",");
                response.append("\"nationality\":\"").append(resultSet.getString("nationality")).append("\"");
                response.append("},");
            }
            if (response.length() > 1 && response.charAt(response.length() - 1) == ',') {
                response.deleteCharAt(response.length() - 1); // Elimina la última coma
            }
            response.append("]}");

        } catch (SQLException e) {
            System.err.println("Error al obtener datos de jugadores: " + e.getMessage());
            return "{\"error\":\"Error al obtener datos de jugadores\"}";
        } finally {
            // Cerrar recursos
            try { if (resultSet != null) resultSet.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (preparedStatement != null) preparedStatement.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return response.toString();
    }

    private String obtenerDatosPartidos() {
        MongoClient mongoClient = null;
        try {
            mongoClient = MongoClients.create(MONGO_DB_URL);
            MongoDatabase database = mongoClient.getDatabase(MONGO_DB_NAME);
            MongoCollection<Document> collection = database.getCollection("Partidos");

            List<Document> partidos = (List<Document>) collection.find().into(new ArrayList<Document>());
            JSONArray jsonArray = new JSONArray();
            for (Document partido : partidos) {
                JSONObject jsonObject = new JSONObject(partido.toJson());
                if (jsonObject.has("fechaCreacion")) {
                    Object fechaCreacionObj = jsonObject.get("fechaCreacion");
                    if (fechaCreacionObj instanceof JSONObject) {
                        JSONObject fechaCreacion = (JSONObject) fechaCreacionObj;
                        if (fechaCreacion.has("$date")) {
                            jsonObject.put("fechaCreacion", fechaCreacion.getString("$date"));
                        }
                    }
                }
                jsonArray.put(jsonObject);
            }
            return jsonArray.toString();
        } catch (Exception e) {
            System.err.println("Error al obtener datos de MongoDB (Partidos): " + e.getMessage());
            return "{\"error\":\"Error al obtener datos de partidos\"}";
        } finally {
            if (mongoClient != null) {
                mongoClient.close();
            }
        }
    }

    private String obtenerDatosActualizaciones() {
        MongoClient mongoClient = null;
        try {
            mongoClient = MongoClients.create(MONGO_DB_URL);
            MongoDatabase database = mongoClient.getDatabase(MONGO_DB_NAME);
            MongoCollection<Document> collection = database.getCollection("Actualizaciones");

            List<Document> actualizaciones = (List<Document>) collection.find().into(new ArrayList<Document>());
            JSONArray jsonArray = new JSONArray();
            for (Document actualizacion : actualizaciones) {
                JSONObject jsonObject = new JSONObject(actualizacion.toJson());
                if (jsonObject.has("fecha")) {
                    Object fechaObj = jsonObject.get("fecha");
                    if (fechaObj instanceof JSONObject) {
                        JSONObject fecha = (JSONObject) fechaObj;
                        if (fecha.has("$date")) {
                            jsonObject.put("fecha", fecha.getString("$date"));
                        }
                    }
                }
                jsonArray.put(jsonObject);
            }
            return jsonArray.toString();
        } catch (Exception e) {
            System.err.println("Error al obtener datos de MongoDB (Actualizaciones): " + e.getMessage());
            return "{\"error\":\"Error al obtener datos de actualizaciones\"}";
        } finally {
            if (mongoClient != null) {
                mongoClient.close();
            }
        }
    }




    private void guardarDatosJugadorEnMariaDB(JSONObject jugador) throws SQLException {
        Connection connection =null;
        PreparedStatement checkStatement = null;
        PreparedStatement insertStatement = null;
        PreparedStatement updateStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(MARIA_DB_URL, MARIA_DB_USER, MARIA_DB_PASSWORD);
            String checkQuery = "SELECT COUNT(*) FROM Players WHERE id = ?";
            checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setInt(1, jugador.getInt("id"));
            resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count == 0) {
                String insertQuery = "INSERT INTO Players (id, name, url, ranking, points, height, nationality, birthplace, birthdate, market_value, hand, side) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setInt(1, jugador.getInt("id"));
                insertStatement.setString(2, jugador.getString("name"));
                insertStatement.setString(3, jugador.isNull("url") ? "" : jugador.getString("url"));
                insertStatement.setInt(4, jugador.isNull("ranking") ? 0 : jugador.getInt("ranking"));
                insertStatement.setInt(5, jugador.isNull("points") ? 0 : jugador.getInt("points"));
                insertStatement.setInt(6, jugador.isNull("height") ? 0 : jugador.getInt("height"));
                insertStatement.setString(7, jugador.getString("nationality"));
                insertStatement.setString(8, jugador.isNull("birthplace") ? null : jugador.getString("birthplace"));
                insertStatement.setDate(9, null);
                insertStatement.setString(10, jugador.isNull("market_value") ? null : jugador.getString("market_value"));
                insertStatement.setString(11, jugador.getString("hand"));
                insertStatement.setString(12, jugador.getString("side"));

                insertStatement.executeUpdate();
                System.out.println("Jugador " + jugador.getString("name") + " guardado en MariaDB.");
            } else {
                String updateQuery = "UPDATE Players SET name = ?, url = ?, ranking = ?, points = ?, height = ?, nationality = ?, birthplace = ?, birthdate = ?, market_value = ?, hand = ?, side = ? WHERE id = ?";
                updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setString(1, jugador.getString("name"));
                updateStatement.setString(2, jugador.isNull("url") ? "" : jugador.getString("url"));
                updateStatement.setInt(3, jugador.isNull("ranking") ? 0 : jugador.getInt("ranking"));
                updateStatement.setInt(4, jugador.isNull("points") ? 0 : jugador.getInt("points"));
                updateStatement.setInt(5, jugador.isNull("height") ? 0 : jugador.getInt("height"));
                updateStatement.setString(6, jugador.getString("nationality"));
                updateStatement.setString(7, jugador.isNull("birthplace") ? null : jugador.getString("birthplace"));
                updateStatement.setDate(8, null);
                updateStatement.setString(9, jugador.isNull("market_value") ? null : jugador.getString("market_value"));
                updateStatement.setString(10, jugador.getString("hand"));
                updateStatement.setString(11, jugador.getString("side"));
                updateStatement.setInt(12, jugador.getInt("id"));

                updateStatement.executeUpdate();
                System.out.println("Jugador " + jugador.getString("name") + " actualizado en MariaDB.");
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (resultSet != null) try {
                resultSet.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
            if (checkStatement != null) try {
                checkStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (insertStatement != null) try {
                insertStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (updateStatement != null) try {
                updateStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (connection != null) try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void guardarDatosPartidoEnMongoDB(String partidoId, String marcadorActual, String estadoPartido, String setsActuales, String puntosActuales, String[] jugadoresActuales) {
        MongoClient mongoClient = null;
        try {
            mongoClient = MongoClients.create(MONGO_DB_URL);
            MongoDatabase database = mongoClient.getDatabase(MONGO_DB_NAME);
            MongoCollection<Document> collection = database.getCollection("Partidos");

            Document partido = new Document("partidoId", partidoId)
                    .append("marcadorActual", marcadorActual)
                    .append("estado", estadoPartido)
                    .append("sets", setsActuales)
                    .append("puntos", puntosActuales)
                    .append("jugadores", Arrays.asList(jugadoresActuales))
                    .append("fechaCreacion", new Date());

            collection.insertOne(partido);
            System.out.println("✅ Partido guardado en MongoDB.");
        } catch (Exception e) {
            System.err.println("❌ Error al guardar partido en MongoDB: " + e.getMessage());
        } finally {
            if (mongoClient != null) {
                mongoClient.close();
            }
        }
    }

    private void guardarActualizacionPartidoEnMongoDB(String idActualizacion, String partidoId, String tipoActualizacion, String datosActualizacion, Date fechaActualizacion) {
        MongoClient mongoClient = null;
        try {
            mongoClient = MongoClients.create(MONGO_DB_URL);
            MongoDatabase database = mongoClient.getDatabase(MONGO_DB_NAME);
            MongoCollection<Document> collection = database.getCollection("Actualizaciones");

            Document actualizacion = new Document("idActualizacion", idActualizacion)
                    .append("partidoId", partidoId)
                    .append("tipo", tipoActualizacion)
                    .append("datos", datosActualizacion)
                    .append("fecha", fechaActualizacion);

            collection.insertOne(actualizacion);
            System.out.println("✅ Actualización guardada en MongoDB.");
        } catch (Exception e) {
            System.err.println("❌ Error al guardar actualización en MongoDB: " + e.getMessage());
        } finally {
            if (mongoClient != null) {
                mongoClient.close();
            }
        }
    }
}*/
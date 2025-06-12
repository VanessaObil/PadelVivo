package com.padel.api_padel.client;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Componente de cliente HTTP para consumir la API de Fantasy Padel Tour.
 *
 * Se encarga de realizar llamadas GET autenticadas y devolver la respuesta en formato JSON como cadena.
 */
@Component
public class ApiClient {

    // Clave de API utilizada para autenticar las solicitudes
    private final String API_KEY = "GJXlm0fpguooqz17mWopqyuCkXJS36GX59f8SvpDc5952360";

    // URL base de la API
    private final String BASE_URL = "https://en.fantasypadeltour.com/api";

    /**
     * Realiza una solicitud HTTP GET a un endpoint de la API o a una URL completa.
     *
     * @param endpointOrFullUrl Puede ser un endpoint relativo (ej. "/players") o una URL completa.
     * @return La respuesta JSON de la API en forma de cadena.
     * @throws IOException Si ocurre un error de conexión o si la respuesta no es exitosa (código diferente de 200).
     */
    public String callApi(String endpointOrFullUrl) throws IOException {

        // Si el parámetro comienza con "http", se asume que es una URL completa
        // De lo contrario, se concatena con la URL base
        String fullUrl = endpointOrFullUrl.startsWith("http")
                ? endpointOrFullUrl
                : BASE_URL + endpointOrFullUrl;

        URL url = new URL(fullUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            // Configura el método de solicitud y los encabezados necesarios
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);

            // Código de estado HTTP de la respuesta
            int responseCode = connection.getResponseCode();
            System.out.println("Código de respuesta para " + fullUrl + ": " + responseCode);

            // Si la respuesta no es 200 OK, lanza una excepción
            if (responseCode != 200) {
                throw new IOException("Error HTTP " + responseCode + ": No se pudo obtener respuesta de la API");
            }

            // Lee la respuesta línea por línea y la acumula en un StringBuilder
            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            return response.toString();

        } finally {
            // Cierra la conexión HTTP
            connection.disconnect();
        }
    }

    /**
     * Devuelve la URL base configurada para las solicitudes a la API.
     *
     * @return La URL base como cadena.
     */
    public String getBaseUrl() {
        return BASE_URL;
    }
}

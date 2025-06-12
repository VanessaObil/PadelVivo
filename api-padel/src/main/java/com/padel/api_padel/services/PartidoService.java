package com.padel.api_padel.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.padel.api_padel.database.DatabaseManager;
import com.padel.api_padel.repositories.PartidoRepository;
import com.padel.api_padel.repositories.UserRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class PartidoService {

    private final DatabaseManager databaseManager;
    private final PartidoRepository partidoRepository;

    @Autowired
    private EmailServiceImpl emailService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoDatabase mongoDatabase;

    @Autowired
    public PartidoService(DatabaseManager databaseManager, PartidoRepository partidoRepository) {
        this.databaseManager = databaseManager;
        this.partidoRepository = partidoRepository;
    }

    public void guardarPartido(String partidoId, String marcadorActual, String estadoPartido,
                               List<Integer> setsActuales, List<Integer> puntosActuales,
                               List<String> jugadoresActuales, List<String> nacionalidades,
                               Date fechaPartido) {

        // Guardado en MongoDB
        String sets = convertirListaEnterosAString(setsActuales);
        String puntos = convertirListaEnterosAString(puntosActuales);
        String[] jugadores = jugadoresActuales.toArray(new String[0]);
        String[] naciones = nacionalidades.toArray(new String[0]);

        databaseManager.guardarDatosPartidoEnMongoDB(
                partidoId,
                marcadorActual,
                estadoPartido,
                sets,
                puntos,
                jugadores,
                naciones,
                fechaPartido
        );

        // 🔔 Enviar correo
        List<com.padel.api_padel.entity.User> usuarios = userRepository.findAll();

        String[] destinatarios = usuarios.stream()
                .map(com.padel.api_padel.entity.User::getUsername)
                .filter(email -> email.contains("@")) // solo correos válidos
                .toArray(String[]::new);

        String asunto = "🆕 Nuevo Partido: " + partidoId;
        String cuerpo = String.format("""
            Se ha guardado un nuevo partido en la base de datos.
            
            📅 Fecha: %s
            🏓 Estado: %s
            
            """,
                fechaPartido.toString(),
                estadoPartido
        );

        boolean enviado = emailService.sendEmail(destinatarios, asunto, cuerpo);
        System.out.println("📧 Email enviado a usuarios: " + enviado);
    }


    public boolean actualizarPartido(String partidoId, String marcadorActual, String estadoPartido,
                                     List<Integer> setsActuales, List<Integer> puntosActuales,
                                     List<String> jugadoresActuales, List<String> nacionalidades,
                                     Date fechaPartido) {

        // Convertimos las listas de sets y puntos a strings
        String sets = convertirListaEnterosAString(setsActuales);
        String puntos = convertirListaEnterosAString(puntosActuales);

        // Convertimos las listas de jugadores y nacionalidades a arrays
        String[] jugadores = jugadoresActuales.toArray(new String[0]);
        String[] naciones = nacionalidades.toArray(new String[0]);

        // Llamada al método que actualiza el partido en la base de datos
        return databaseManager.actualizarPartidoEnMongoDB(
                partidoId,
                marcadorActual,
                estadoPartido,
                sets,
                puntos,
                jugadores,
                naciones,
                fechaPartido
        );
    }


    public List<Document> obtenerTodosLosPartidos() {
        return databaseManager.obtenerTodosLosPartidos();
    }

    public boolean eliminarPartidoPorPartidoId(String partidoId) {
        MongoCollection<Document> collection = mongoDatabase.getCollection("Partidos");
        Document query = new Document("partidoId", partidoId);
        long resultado = collection.deleteOne(query).getDeletedCount();

        if (resultado > 0) {
            System.out.println("✅ Partido eliminado correctamente: " + partidoId);
            return true;
        } else {
            System.err.println("❌ Partido no encontrado con partidoId: " + partidoId);
            return false;
        }
    }

    // Utilidad para convertir lista de enteros a string "6,4,7"
    private String convertirListaEnterosAString(List<Integer> lista) {
        if (lista == null || lista.isEmpty()) return "";
        return lista.stream()
                .map(String::valueOf)
                .reduce((a, b) -> a + "," + b)
                .orElse("");
    }
}

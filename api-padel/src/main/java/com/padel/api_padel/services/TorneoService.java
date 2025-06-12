package com.padel.api_padel.services;

import com.padel.api_padel.entity.Torneo;
import com.padel.api_padel.entity.User;
import com.padel.api_padel.repositories.TemporadaRepository;
import com.padel.api_padel.repositories.TorneoRepository;
import com.padel.api_padel.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con los torneos.
 * Este servicio permite obtener, guardar y eliminar torneos en la base de datos.
 */
@Service
public class TorneoService {

    @Autowired
    private EmailServiceImpl emailService;
    @Autowired
    private UserRepository usuarioRepository;

    // Repositorio de torneos para interactuar con la base de datos
    @Autowired
    private TorneoRepository torneoRepository;

    // Repositorio de temporadas para verificar si la temporada existe antes de guardar un torneo
    @Autowired
    private TemporadaRepository temporadaRepository;

    /**
     * Método para guardar un nuevo torneo en la base de datos.
     *
     * @param torneo El torneo que se desea guardar
     * @return El torneo guardado
     * @throws RuntimeException Si la temporada proporcionada no existe en la base de datos
     */


    public Torneo guardarTorneo(Torneo torneo) {
        System.out.println("🟡 Intentando guardar torneo: " + torneo.getName());

        if (torneo.getTemporada() == null || !temporadaRepository.existsById(torneo.getTemporada().getId())) {
            System.out.println("⚠️ La temporada proporcionada no existe: " + torneo.getTemporada());
            throw new RuntimeException("La temporada proporcionada no existe.");
        }

        torneoRepository.save(torneo);
        System.out.println("✅ Torneo guardado correctamente en la base de datos.");

        // Obtener usuarios
        List<User> usuarios = usuarioRepository.findAll();
        System.out.println("🔍 Usuarios encontrados: " + usuarios.size());

        // Expresión regular simple para validar emails
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

        // Filtrar sólo emails válidos
        List<String> emailsValidos = usuarios.stream()
                .map(User::getUsername)  // Cambia a getEmail() si tienes otro campo para correo
                .filter(email -> emailPattern.matcher(email).matches())
                .peek(email -> System.out.println("✅ Email válido: " + email))
                .toList();

        if (emailsValidos.isEmpty()) {
            System.out.println("❌ No hay correos válidos para enviar el email.");
            return torneo;
        }

        String[] destinatarios = emailsValidos.toArray(new String[0]);
        System.out.println("📧 Correos destinatarios: " + String.join(", ", destinatarios));

        String asunto = "Nuevo torneo: " + torneo.getName();
        String cuerpo = String.format(
                "Se ha creado un nuevo torneo:\n\n" +
                        "🏆 Nombre: %s\n📍 Ubicación: %s\n📅 Inicio: %s\n📅 Fin: %s\nTemporada ID: %d",
                torneo.getName(),
                torneo.getLocation(),
                torneo.getStartDate(),
                torneo.getEndDate(),
                torneo.getTemporada().getId()
        );

        System.out.println("🚀 Enviando email a los usuarios...");
        boolean enviado = emailService.sendEmail(destinatarios, asunto, cuerpo);

        if (enviado) {
            System.out.println("✅ Emails enviados correctamente.");
        } else {
            System.out.println("❌ Error al enviar los emails.");
        }

        return torneo;
    }




    /**
     * Método para obtener todos los torneos almacenados en la base de datos.
     *
     * @return Una lista de todos los torneos en la base de datos
     */
    public List<Torneo> obtenerTodosLosTorneos() {
        return torneoRepository.findAll();
    }

    /**
     * Método para obtener los torneos asociados a una temporada específica.
     *
     * @param temporadaId El ID de la temporada para obtener los torneos asociados
     * @return Una lista de torneos correspondientes a la temporada especificada
     */
    public List<Torneo> obtenerTorneosPorTemporada(Integer temporadaId) {
        return torneoRepository.findByTemporada_Id(temporadaId);
    }

    /**
     * Método para eliminar un torneo de la base de datos por su ID.
     *
     * @param id El ID del torneo a eliminar
     * @return true si el torneo fue eliminado correctamente, false si no fue encontrado
     */
    public boolean eliminarTorneo(Integer id) {
        if (torneoRepository.existsById(id)) {
            torneoRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public Torneo actualizarTorneo(Integer id, Torneo torneoActualizado) {
        // Buscar el torneo original
        Torneo torneoExistente = torneoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado con ID: " + id));

        // Actualizar campos
        torneoExistente.setName(torneoActualizado.getName());
        torneoExistente.setLocation(torneoActualizado.getLocation());
        torneoExistente.setStartDate(torneoActualizado.getStartDate());
        torneoExistente.setEndDate(torneoActualizado.getEndDate());

        // Solo si permite cambiar de temporada
        if (torneoActualizado.getTemporada() != null &&
                temporadaRepository.existsById(torneoActualizado.getTemporada().getId())) {
            torneoExistente.setTemporada(torneoActualizado.getTemporada());
        }


        // Guardar cambios
        return torneoRepository.save(torneoExistente);
    }

}

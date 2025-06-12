package com.padel.api_padel.controllers;

import com.padel.api_padel.services.ActualizacionService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Controlador REST para gestionar actualizaciones de partidos.
 *
 * Expone endpoints para guardar y recuperar actualizaciones relacionadas con partidos.
 */
@RestController
@RequestMapping("/api/actualizaciones") // Prefijo común para todos los endpoints de esta clase
public class ActualizacionController {

    private final ActualizacionService actualizacionService;

    /**
     * Constructor que inyecta el servicio de actualizaciones.
     *
     * @param actualizacionService servicio encargado de la lógica de actualizaciones
     */
    @Autowired
    public ActualizacionController(ActualizacionService actualizacionService) {
        this.actualizacionService = actualizacionService;
    }

    /**
     * Endpoint para guardar una actualización de partido.
     *
     * @param idActualizacion ID de la actualización
     * @param partidoId ID del partido al que corresponde la actualización
     * @param tipoActualizacion tipo de la actualización (ej: "Gol", "Falta", etc.)
     * @param datosActualizacion datos adicionales de la actualización (en formato JSON o texto)
     * @param fechaActualizacion (opcional) fecha de la actualización
     * @return mensaje de éxito con código HTTP 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<String> guardarActualizacion(
            @RequestParam String idActualizacion,
            @RequestParam String partidoId,
            @RequestParam String tipoActualizacion,
            @RequestParam String datosActualizacion,
            @RequestParam(required = false) Date fechaActualizacion) {
        // La lógica para guardar la actualización está comentada y no se ejecuta actualmente
        // actualizacionService.guardarActualizacion(idActualizacion, partidoId, tipoActualizacion, datosActualizacion, fechaActualizacion);
        return new ResponseEntity<>("Actualización guardada correctamente", HttpStatus.CREATED);
    }

    /**
     * Endpoint para obtener todas las actualizaciones registradas.
     *
     * @return lista de documentos BSON con todas las actualizaciones almacenadas y código HTTP 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<Document>> obtenerActualizaciones() {
        List<Document> actualizaciones = actualizacionService.obtenerTodasLasActualizaciones();
        return new ResponseEntity<>(actualizaciones, HttpStatus.OK);
    }

}

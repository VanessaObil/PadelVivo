package com.padel.api_padel.controllers;

import com.padel.api_padel.entity.Torneo;
import com.padel.api_padel.services.TorneoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de torneos.
 * Permite crear, obtener y eliminar torneos.
 */
@CrossOrigin(origins = {"http://localhost:4200", "http://front-padelvivo1.s3-website-us-east-1.amazonaws.com"}) // <--- ¡ASEGÚRATE DE QUE ESTA URL SEA EXACTA!
@RestController
@RequestMapping("/api/torneos") // Ruta base para los endpoints relacionados con torneos
public class TorneoController {

    private final TorneoService torneoService;

    /**
     * Constructor que inyecta el servicio de torneos.
     *
     * @param torneoService servicio con la lógica de negocio para torneos
     */
    @Autowired
    public TorneoController(TorneoService torneoService) {
        this.torneoService = torneoService;
    }

    /**
     * Guarda un nuevo torneo en la base de datos.
     *
     * @param torneo objeto torneo enviado en el cuerpo de la solicitud
     * @return el torneo guardado con código HTTP 201 (CREATED) o error 400 en caso de fallo
     */
    @PostMapping
    public ResponseEntity<Torneo> guardarTorneo(@RequestBody Torneo torneo) {
        try {
            // Imprime un mensaje en consola indicando que se está guardando el torneo
            System.out.println("Guardando torneo: " + torneo.getName());

            // Guarda el torneo usando el servicio
            Torneo nuevoTorneo = torneoService.guardarTorneo(torneo);

            // Devuelve el torneo guardado con código HTTP 201
            return new ResponseEntity<>(nuevoTorneo, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Si ocurre un error, se imprime en consola y se devuelve un error HTTP 400
            System.out.println("Error al guardar torneo: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Obtiene todos los torneos disponibles.
     *
     * @return lista de torneos con código HTTP 200
     */
    @GetMapping
    public ResponseEntity<List<Torneo>> obtenerTodosLosTorneos() {
        List<Torneo> torneos = torneoService.obtenerTodosLosTorneos();
        return new ResponseEntity<>(torneos, HttpStatus.OK);
    }

    /**
     * Obtiene los torneos de una temporada específica.
     *
     * @param temporadaId identificador de la temporada
     * @return lista de torneos pertenecientes a la temporada con código HTTP 200
     */
    @GetMapping("/temporada/{temporadaId}")
    public ResponseEntity<List<Torneo>> obtenerTorneosPorTemporada(@PathVariable Integer temporadaId) {
        List<Torneo> torneos = torneoService.obtenerTorneosPorTemporada(temporadaId);
        return new ResponseEntity<>(torneos, HttpStatus.OK);
    }

    /**
     * Elimina un torneo por su ID.
     *
     * @param id identificador del torneo a eliminar
     * @return código HTTP 204 si el torneo fue eliminado correctamente, o 404 si no se encontró el torneo
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTorneo(@PathVariable Integer id) {
        boolean eliminado = torneoService.eliminarTorneo(id);
        if (eliminado) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Torneo> actualizarTorneo(@PathVariable Integer id, @RequestBody Torneo torneoActualizado) {
        System.out.println("PUT recibido para torneo ID: " + id + " con body: " + torneoActualizado);
        try {
            Torneo actualizado = torneoService.actualizarTorneo(id, torneoActualizado);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            System.out.println("Error en actualizarTorneo: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


}

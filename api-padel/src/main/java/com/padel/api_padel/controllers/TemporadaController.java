package com.padel.api_padel.controllers;

import com.padel.api_padel.entity.Temporada;
import com.padel.api_padel.repositories.TemporadaRepository;
import com.padel.api_padel.services.TemporadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de temporadas.
 *
 * Permite obtener, guardar, actualizar y eliminar temporadas.
 */
@CrossOrigin(origins = "http://localhost:4200") // Habilita CORS para el frontend local
@RestController
@RequestMapping("/api/temporadas") // Ruta base para los endpoints relacionados con temporadas
public class TemporadaController {

    private final TemporadaService temporadaService;

    @Autowired
    private TemporadaRepository temporadaRepository; // Repositorio para acceso directo a la base de datos

    /**
     * Constructor que inyecta el servicio de temporadas.
     *
     * @param temporadaService servicio con la lógica de negocio para temporadas
     */
    @Autowired
    public TemporadaController(TemporadaService temporadaService) {
        this.temporadaService = temporadaService;
    }

    /**
     * Obtiene todas las temporadas disponibles.
     *
     * @return lista de temporadas con código HTTP 200
     */
    @GetMapping
    public ResponseEntity<List<Temporada>> getAllTemporadas() {
        return new ResponseEntity<>(temporadaService.getAllTemporadas(), HttpStatus.OK);
    }

    /**
     * Guarda una nueva temporada.
     *
     * @param temporada objeto temporada enviado en el cuerpo de la solicitud
     * @return mensaje de confirmación con código 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<String> guardarTemporada(@RequestBody Temporada temporada) {
        temporadaService.guardarTemporada(temporada);
        return new ResponseEntity<>("Temporada guardada correctamente", HttpStatus.CREATED);
    }

    /**
     * Elimina una temporada por su ID.
     *
     * @param id identificador de la temporada
     * @return 204 si se eliminó correctamente, 404 si no se encontró
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTemporada(@PathVariable Integer id) {
        boolean eliminado = temporadaService.eliminarTemporada(id);
        if (eliminado) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Actualiza una temporada existente.
     *
     * @param id ID de la temporada a actualizar
     * @param temporada datos nuevos a actualizar
     * @return la temporada actualizada si existe, 404 en caso contrario
     */
    @PutMapping("/{id}")
    public ResponseEntity<Temporada> actualizarTemporada(@PathVariable Integer id, @RequestBody Temporada temporada) {
        // Verifica si la temporada existe antes de actualizar
        if (temporadaRepository.existsById(id)) {
            // Obtiene la temporada existente y actualiza sus campos
            Temporada temporadaExistente = temporadaRepository.findById(id).orElseThrow();
            temporadaExistente.setName(temporada.getName());
            temporadaExistente.setStartDate(temporada.getStartDate());
            temporadaExistente.setEndDate(temporada.getEndDate());
            temporadaExistente.setYear(temporada.getYear());

            // Guarda y devuelve la temporada actualizada
            Temporada temporadaActualizada = temporadaRepository.save(temporadaExistente);
            return new ResponseEntity<>(temporadaActualizada, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

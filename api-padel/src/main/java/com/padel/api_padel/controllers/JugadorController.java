package com.padel.api_padel.controllers;

import com.padel.api_padel.entity.Jugador;
import com.padel.api_padel.entity.Temporada;
import com.padel.api_padel.repositories.JugadorRepository;
import com.padel.api_padel.services.JugadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar jugadores.
 *
 * Proporciona endpoints para listar, agregar y eliminar jugadores.
 */
@RestController
@RequestMapping("/api/jugadores") // Ruta base para todos los endpoints relacionados con jugadores
@CrossOrigin(origins = "http://localhost:4200") // Permite solicitudes CORS desde el frontend en localhost:4200
public class JugadorController {

    private final JugadorService jugadorService;
    private final JugadorRepository jugadorRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param jugadorService servicio que gestiona la lógica relacionada con jugadores
     */
    @Autowired
    public JugadorController(JugadorService jugadorService, JugadorRepository jugadorRepository) {
        this.jugadorService = jugadorService;
        this.jugadorRepository = jugadorRepository;
    }

    /**
     * Obtiene la lista de todos los jugadores almacenados.
     *
     * @return lista de jugadores con código HTTP 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<Jugador>> obtenerTodosLosJugadores() {
        List<Jugador> jugadores = jugadorService.obtenerTodosLosJugadores();
        return ResponseEntity.ok(jugadores);
    }

    /**
     * Elimina un jugador por su ID.
     *
     * @param id ID del jugador a eliminar
     * @return 204 No Content si fue eliminado, 404 Not Found si no se encontró
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarJugador(@PathVariable Long id) {
        boolean eliminado = jugadorService.eliminarJugador(id);
        if (eliminado) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Guarda un nuevo jugador.
     *
     * @param jugador objeto Jugador enviado en el cuerpo de la solicitud
     * @return mensaje de éxito con código 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<String> guardarJugador(@RequestBody Jugador jugador) {
        jugadorService.guardarJugador(jugador);
        return new ResponseEntity<>("Jugador guardada correctamente", HttpStatus.CREATED);
    }

    // Método alternativo comentado que devuelve el jugador creado en lugar de un mensaje
    // @PostMapping("/jugadores")
    // public ResponseEntity<Jugador> guardarJugador(@RequestBody Jugador jugador) {
    //     Jugador nuevoJugador = jugadorService.guardarJugador(jugador);
    //     return ResponseEntity.status(HttpStatus.CREATED).body(nuevoJugador);
    // }

    @PutMapping("/{id}")
    public ResponseEntity<Jugador> actualizarJugador(@PathVariable Long id, @RequestBody Jugador jugador) {
        return jugadorService.actualizarJugador(id, jugador)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

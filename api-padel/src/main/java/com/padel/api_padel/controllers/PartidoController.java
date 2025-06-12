package com.padel.api_padel.controllers;

import com.padel.api_padel.dtos.PartidoDTO;
import com.padel.api_padel.services.PartidoService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {
        "http://localhost:4200",
        "http://frontend-padelvivo.s3-website-us-east-1.amazonaws.com"
})
@RestController
@RequestMapping("/api/partidos")
public class PartidoController {

    private final PartidoService partidoService;

    @Autowired
    public PartidoController(PartidoService partidoService) {
        this.partidoService = partidoService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> guardarPartido(@RequestBody PartidoDTO dto) {
        Date fechaSql = Date.valueOf(dto.fechaPartido);

        partidoService.guardarPartido(
                dto.partidoId,
                dto.marcadorActual,
                dto.estadoPartido,
                dto.setsActuales,
                dto.puntosActuales,
                dto.jugadoresActuales,
                dto.nacionalidades,
                fechaSql
        );

        Map<String, String> response = new HashMap<>();
        response.put("message", "Partido creado");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PutMapping("/{partidoId}")
    public ResponseEntity<Map<String, String>> actualizarPartido(@PathVariable String partidoId, @RequestBody PartidoDTO dto) {
        if (dto.getJugadoresActuales() == null || dto.getJugadoresActuales().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Los jugadores no pueden estar vacíos");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        Date fechaSql = Date.valueOf(dto.getFechaPartido());

        boolean actualizado = partidoService.actualizarPartido(
                partidoId,
                dto.getMarcadorActual(),
                dto.getEstadoPartido(),
                dto.getSetsActuales(),
                dto.getPuntosActuales(),
                dto.getJugadoresActuales(),
                dto.getNacionalidades(),
                fechaSql
        );

        Map<String, String> respuesta = new HashMap<>();
        if (actualizado) {
            respuesta.put("mensaje", "Partido actualizado correctamente");
            return ResponseEntity.ok(respuesta);
        } else {
            respuesta.put("error", "Partido no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }
    }


    @GetMapping
    public ResponseEntity<List<Document>> obtenerPartidos() {
        List<Document> partidos = partidoService.obtenerTodosLosPartidos();
        return new ResponseEntity<>(partidos, HttpStatus.OK);
    }

    @DeleteMapping("/{partidoId}")
    public ResponseEntity<String> eliminarPartido(@PathVariable String partidoId) {
        boolean eliminado = partidoService.eliminarPartidoPorPartidoId(partidoId);
        if (eliminado) {
            return ResponseEntity.ok("Partido eliminado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Partido no encontrado");
        }
    }
}

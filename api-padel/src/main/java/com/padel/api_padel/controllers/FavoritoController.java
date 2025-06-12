package com.padel.api_padel.controllers;

import com.padel.api_padel.services.FavoritoService;
import com.padel.api_padel.utils.SecurityUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    @PostMapping
    public ResponseEntity<?> agregar(@RequestParam String matchId) {
        try {
            Long userId = SecurityUtils.getCurrentUserId();
            favoritoService.agregarAFavoritos(userId, matchId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("{\"message\":\"Agregado a favoritos\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al agregar favorito: " + e.getMessage() + "\"}");
        }
    }


    @Transactional
    @DeleteMapping
    public ResponseEntity<Void> eliminar(@RequestParam String matchId) {
        try {
            Long userId = SecurityUtils.getCurrentUserId();
            favoritoService.eliminarDeFavoritos(userId, matchId);
            return ResponseEntity.noContent().build();  // 204 No Content
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }


    @GetMapping
    public ResponseEntity<List<Document>> obtenerTodos() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(favoritoService.obtenerFavoritosPorUsuario(userId));
    }

    @GetMapping("/existe")
    public ResponseEntity<Boolean> estaEnFavoritos(@RequestParam String matchId) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(favoritoService.estaEnFavoritos(userId, matchId));
    }
}

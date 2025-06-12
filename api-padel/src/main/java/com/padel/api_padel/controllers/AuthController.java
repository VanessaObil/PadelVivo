package com.padel.api_padel.controllers;

import com.padel.api_padel.dtos.RegisterRequest;
import com.padel.api_padel.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador REST para la autenticación de usuarios.
 *
 * Actualmente expone el endpoint de registro de nuevos usuarios.
 */
@RestController
@RequestMapping("/api/auth") // Prefijo común para las rutas de autenticación
@CrossOrigin(origins = "http://frontend-padelvivo.s3-website-us-east-1.amazonaws.com") // Permite CORS desde el frontend en Angular (localhost:4200)
public class AuthController {

    @Autowired
    private AuthService authService; // Servicio encargado de la lógica de autenticación y registro

    /**
     * Endpoint para registrar un nuevo usuario.
     *
     * @param request objeto que contiene los datos del nuevo usuario (nombre, email, contraseña, etc.)
     * @return 200 OK si se registró correctamente, 400 Bad Request si hubo algún error
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            authService.register(request); // Llama al servicio de registro
            return ResponseEntity.ok(Map.of("message", "Usuario registrado exitosamente"));
        } catch (RuntimeException e) {
            // Si ocurre una excepción durante el registro, se devuelve un mensaje de error
            return ResponseEntity.status(400).body(Map.of("error", "Error al registrar usuario: " + e.getMessage()));
        }
    }

}

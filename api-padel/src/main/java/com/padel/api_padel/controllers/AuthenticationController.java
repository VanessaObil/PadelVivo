package com.padel.api_padel.controllers;

import com.padel.api_padel.dtos.AuthRequestDTO;
import com.padel.api_padel.dtos.AuthResponseDTO;
import com.padel.api_padel.entity.User;
import com.padel.api_padel.repositories.UserRepository;
import com.padel.api_padel.utils.JWTUtil;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponseDTO> authenticate(@Valid @RequestBody AuthRequestDTO authRequest){
        try{
            if (authRequest.getUsername() == null || authRequest.getPassword() == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new AuthResponseDTO(null, "El nombre de usuario y la contraseña son obligatorios."));
            }
            Authentication authentication= authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword())
            );
            String username = authentication.getName();

            // Obtén el usuario completo
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Obtener roles desde el usuario o desde la autenticación
            List<String> roles = user.getRoles().stream()
                    .map(role -> role.getName())
                    .toList();

            // Genera el token usando usuario completo (modifica jwtUtil para esto)
            String token = jwtUtil.generateToken(user);

            return ResponseEntity.ok(new AuthResponseDTO(token, "Authentication successful"));

        }catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponseDTO(null, "Credenciales invalidas. Por favor verifica tus datos."));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthResponseDTO(null, "Ocurrio un error inesperado. Por favor, intente de nuevo mas tarde."));
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AuthResponseDTO> handleException(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new AuthResponseDTO(null, "Ocurrio un error inesperado: " + e.getMessage()));
    }
}

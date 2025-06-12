package com.padel.api_padel.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) que representa la solicitud de registro de un usuario.
 * Este objeto es utilizado para transferir los datos del usuario durante el proceso de registro.
 *
 * Lombok genera automáticamente los métodos getter y setter para los campos de esta clase.
 */
@Getter
@Setter
public class RegisterRequest {

    /**
     * Nombre de usuario del nuevo usuario.
     * Este será el identificador único del usuario en el sistema.
     */
    private String username;

    /**
     * Contraseña del nuevo usuario.
     * Esta será utilizada para autenticar al usuario en el sistema.
     */
    private String password;

    /**
     * Primer nombre del nuevo usuario.
     * Se utiliza para personalizar la experiencia del usuario en el sistema.
     */
    private String firstName;

    /**
     * Apellido del nuevo usuario.
     * Se utiliza junto con el primer nombre para personalizar la experiencia del usuario.
     */
    private String lastName;

    // A continuación, hay algunos campos que están comentados. Pueden ser añadidos si se necesitan:

    // private String email; // Correo electrónico del nuevo usuario.
    // private String image; // Imagen de perfil del nuevo usuario.

}

package com.padel.api_padel.services;


import com.padel.api_padel.dtos.RegisterRequest;
import com.padel.api_padel.entity.Role;
import com.padel.api_padel.entity.User;
import com.padel.api_padel.repositories.RoleRepository;
import com.padel.api_padel.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;  // Repositorio de usuarios, usado para realizar operaciones en la base de datos.

    @Autowired
    private RoleRepository roleRepository;  // Repositorio de roles, usado para obtener los roles de los usuarios.

    @Autowired
    private PasswordEncoder passwordEncoder;  // Codificador de contraseñas, usado para encriptar las contraseñas de los usuarios.

    /**
     * Método para registrar un nuevo usuario.
     *
     * Este método valida si el nombre de usuario ya existe, obtiene el rol 'ROLE_USER' de la base de datos,
     * encripta la contraseña del usuario y guarda la información del nuevo usuario en la base de datos.
     *
     * @param request objeto con los datos de registro del usuario.
     * @throws RuntimeException si el nombre de usuario ya está en uso o si el rol 'ROLE_USER' no existe.
     */
    public void register(RegisterRequest request) {

        // Verificación de si el nombre de usuario ya existe en la base de datos
        if (userRepository.existsByUsername(request.getUsername())) {
            // Si ya existe el nombre de usuario, se lanza una excepción
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        // Obtener el rol 'ROLE_USER' desde la base de datos
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("El rol ROLE_USER no existe"));

        // Crear el nuevo objeto 'User' con los datos de la solicitud de registro
        User user = new User();
        user.setUsername(request.getUsername());  // Establecer el nombre de usuario
        user.setPassword(passwordEncoder.encode(request.getPassword()));  // Encriptar la contraseña antes de guardarla
        user.setFirstName(request.getFirstName());  // Establecer el primer nombre
        user.setLastName(request.getLastName());    // Establecer el apellido

        // Establecer que el usuario está habilitado para iniciar sesión
        user.setEnabled(true);

        // Asignar el rol 'ROLE_USER' al usuario
        user.setRoles(Collections.singleton(userRole));  // Asignación de un conjunto de roles, en este caso solo 'ROLE_USER'

        // Establecer las fechas de creación y de último cambio de contraseña
        user.setCreatedDate(new Timestamp(System.currentTimeMillis()).toLocalDateTime());  // Fecha actual de creación
        user.setLastPasswordChangeDate(new Timestamp(System.currentTimeMillis()).toLocalDateTime());  // Fecha actual de último cambio de contraseña

        // Guardar el usuario en la base de datos
        userRepository.save(user);
    }

}

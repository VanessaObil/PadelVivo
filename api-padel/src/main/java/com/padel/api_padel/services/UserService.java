package com.padel.api_padel.services;


import com.padel.api_padel.dtos.AuthResponseDTO;
import com.padel.api_padel.dtos.UserDTO;
import com.padel.api_padel.dtos.UserUpdateDTO;
import com.padel.api_padel.entity.Role;
import com.padel.api_padel.entity.User;
import com.padel.api_padel.repositories.RoleRepository;
import com.padel.api_padel.repositories.UserRepository;
import com.padel.api_padel.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;



    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, JWTUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);
        return convertToDTO(saved);
    }

    public Optional<UserDTO> updateUser(Long id, User updatedUserData) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        User user = optionalUser.get();

        // Actualizar campos editables
        user.setFirstName(updatedUserData.getFirstName());
        user.setLastName(updatedUserData.getLastName());
        user.setImage(updatedUserData.getImage());

        // Codificar contraseña si se actualiza
        if (updatedUserData.getPassword() != null && !updatedUserData.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(updatedUserData.getPassword()));
        }

        User savedUser = userRepository.save(user);
        return Optional.of(convertToDTO(savedUser));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserDTO convertToDTO(User user) {
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getImage(),
                user.isEnabled(),
                user.getPassword(),
                user.getCreatedDate(),
                user.getLastModifiedDate(),
                user.getLastPasswordChangeDate(),
                roleNames
        );
    }

    public UserUpdateDTO convertToUpdateDTO(User user) {
        return new UserUpdateDTO(

                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getImage(),

                user.getPassword()

        );
    }

    @Transactional
    public AuthResponseDTO createUserAndAuthenticate(User user) {
        String rawPassword = user.getPassword();  // Contraseña sin codificar

        // Codificar contraseña
        user.setPassword(passwordEncoder.encode(rawPassword));

        // 🔑 Asignar el rol por defecto
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Rol ROLE_USER no encontrado"));

        user.setRoles(Set.of(userRole));
        user.setEnabled(true); // Asegura que el usuario esté habilitado

        User savedUser = userRepository.save(user);

        // Autenticación con la contraseña original
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), rawPassword)
        );

        String token = jwtUtil.generateToken(savedUser);

        return new AuthResponseDTO(token, "Usuario creado y autenticado con éxito");
    }

    // perfil user
    public Optional<UserDTO> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::convertToDTO);
    }

    public Optional<UserDTO> updateUserByUsername(String username, User updatedUserData) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        User user = optionalUser.get();

        // ⚠️ Asegúrate de controlar qué campos puedes actualizar
        user.setFirstName(updatedUserData.getFirstName());
        user.setLastName(updatedUserData.getLastName());
        user.setImage(updatedUserData.getImage());

        // Si quieres permitir cambiar contraseña, recuerda codificarla
        if (updatedUserData.getPassword() != null && !updatedUserData.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(updatedUserData.getPassword()));
        }

        User savedUser = userRepository.save(user);
        return Optional.of(convertToDTO(savedUser));
    }
    public UserDTO addUserWithRole(User user, String roleName) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + roleName));

        user.setRoles(Set.of(role));
        user.setEnabled(true);

        User saved = userRepository.save(user);
        return convertToDTO(saved);
    }

}

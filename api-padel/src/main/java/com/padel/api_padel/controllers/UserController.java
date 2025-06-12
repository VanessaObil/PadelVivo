package com.padel.api_padel.controllers;

import com.padel.api_padel.dtos.AuthResponseDTO;
import com.padel.api_padel.dtos.UserDTO;
import com.padel.api_padel.dtos.UserUpdateDTO;
import com.padel.api_padel.entity.CreateUserRequest;
import com.padel.api_padel.entity.User;

import com.padel.api_padel.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<UserDTO> addUser(@RequestBody CreateUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setImage(request.getImage());
        user.setEnabled(true);

        UserDTO createdUser = userService.addUserWithRole(user, request.getRoleName());
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> registerAndAuthenticate(@RequestBody User user) {
        AuthResponseDTO response = userService.createUserAndAuthenticate(user);
        return ResponseEntity.ok(response);
    }

    /*perfil */


    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDTO> updateProfile(@RequestBody User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userService.updateUserByUsername(username, user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


//    @PutMapping("/{username}")
//    public ResponseEntity<UserDTO> updateUserByUsername(
//            @PathVariable String username,
//            @RequestBody UserUpdate updatedUserData) {
//        return userService.updateUserByUsername(username, updatedUserData)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }

}

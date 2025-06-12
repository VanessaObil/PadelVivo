package com.padel.api_padel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Data
public class CreateUserRequest {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String image;
    private String roleName;  // Aquí el rol que quieras asignar

    // getters y setters
}

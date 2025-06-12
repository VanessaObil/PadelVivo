package com.padel.api_padel.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Data
public class UserUpdateDTO {
        private String username;
        private String firstName;
        private String lastName;
        private String image;
        private String password;
        // getters y setters

}

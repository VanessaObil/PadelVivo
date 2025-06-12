package com.padel.api_padel.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoritoDTO {
    private Long userId;
    private String matchId; // partidoId de MongoDB

    // Getters y Setters
}

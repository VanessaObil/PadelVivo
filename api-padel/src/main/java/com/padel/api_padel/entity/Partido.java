package com.padel.api_padel.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;
@Getter
@Setter
@Document(collection = "partidos")
public class Partido {

    @Id
    private String partidoId;
    private String marcadorActual;
    private String estadoPartido;
    private String setsActuales;
    private String puntosActuales;
    private String[] jugadores;
    private String[] nacionalidades;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaPartido;

    // Getters y Setters
    // (añade aquí todos los métodos getXXX() y setXXX() para los atributos)

}

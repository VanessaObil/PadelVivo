package com.padel.api_padel.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter

public class PartidoDTO {
    public String partidoId;
    public String marcadorActual;
    public String estadoPartido;
    public List<Integer> setsActuales;
    public List<Integer> puntosActuales;
    public List<String> jugadoresActuales;
    public List<String> nacionalidades;

    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate fechaPartido;

}

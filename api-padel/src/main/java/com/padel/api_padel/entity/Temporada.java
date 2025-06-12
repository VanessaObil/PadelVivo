package com.padel.api_padel.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * La clase Temporada representa una temporada en la base de datos MariaDB (o cualquier base de datos compatible con JPA).
 * Esta clase está mapeada a la tabla "Seasons" en la base de datos.
 * Se utiliza la tecnología JPA para la persistencia de datos en la base de datos.
 */
@Getter
@Setter
@Entity
@Table(name = "Seasons")  // Mapeo a la tabla "Seasons" en la base de datos
public class Temporada {

    /**
     * Identificador único de la temporada (clave primaria) en la base de datos.
     * Utiliza la anotación @Id de JPA.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombre de la temporada (por ejemplo, "Temporada 2025").
     */
    private String name;

    /**
     * Fecha de inicio de la temporada.
     * Esta propiedad se mapea a la columna "start_date" en la base de datos.
     */
    @Column(name = "start_date")
    private Date startDate;

    /**
     * Fecha de finalización de la temporada.
     * Esta propiedad se mapea a la columna "end_date" en la base de datos.
     */
    @Column(name = "end_date")
    private Date endDate;

    /**
     * Año de la temporada.
     */
    private Integer year;

    // Getters y Setters generados por Lombok (@Getter y @Setter)
}

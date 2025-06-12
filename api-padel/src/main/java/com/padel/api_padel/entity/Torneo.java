package com.padel.api_padel.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * La clase Torneo representa un torneo en la base de datos, mapeada a la tabla "Tournaments".
 * Se utiliza la tecnología JPA para la persistencia de datos en la base de datos.
 */
@Entity
@Getter
@Setter
@Table(name = "Tournaments")  // Mapeo a la tabla "Tournaments" en la base de datos
public class Torneo {

    /**
     * Identificador único del torneo (clave primaria) en la base de datos.
     * Utiliza la anotación @Id de JPA.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombre del torneo (por ejemplo, "Open de París").
     */
    private String name;

    /**
     * Ubicación del torneo, como la ciudad donde se juega.
     */
    private String location;

    /**
     * País donde se lleva a cabo el torneo.
     */
    private String country;

    /**
     * Nivel del torneo, como "Internacional", "Nacional", etc.
     */
    private String level;

    /**
     * Estado del torneo, como "Activo", "Finalizado", etc.
     */
    private String status;

    /**
     * Fecha de inicio del torneo.
     * Esta propiedad se mapea a la columna "start_date" en la base de datos.
     */
    @Column(name = "start_date")
    private Date startDate;

    /**
     * Fecha de finalización del torneo.
     * Esta propiedad se mapea a la columna "end_date" en la base de datos.
     */
    @Column(name = "end_date")
    private Date endDate;

    /**
     * Relación con la entidad Temporada.
     * Cada torneo debe estar asociado a una temporada (relación muchos a uno).
     * La anotación @ManyToOne indica que muchos torneos pueden pertenecer a una sola temporada.
     * La anotación @JoinColumn se utiliza para especificar la columna en la tabla "Tournaments" que almacenará el id de la temporada.
     */
    @ManyToOne
    @JoinColumn(name = "season_id", nullable = false)  // Esto indica que la relación con Temporada es obligatoria
    private Temporada temporada;


}

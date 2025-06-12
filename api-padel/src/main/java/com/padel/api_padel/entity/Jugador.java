package com.padel.api_padel.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * La clase Jugador representa a un jugador de pádel en la base de datos.
 * Esta clase está mapeada a la tabla "Players" en la base de datos.
 */
@Entity
@Getter
@Setter
@Table(name = "Players")
public class Jugador {

    /**
     * Identificador único del jugador (clave primaria).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombre completo del jugador.
     */
    private String name;

    /**
     * URL que contiene información adicional sobre el jugador.
     */
    private String url;

    /**
     * Ranking del jugador en el circuito.
     */
    private Integer ranking;

    /**
     * Puntos acumulados por el jugador en su carrera.
     */
    private Integer points;

    /**
     * Altura del jugador en centímetros.
     */
    private Integer height;

    /**
     * Nacionalidad del jugador.
     */
    private String nationality;

    /**
     * Lugar de nacimiento del jugador.
     */
    private String birthplace;

    /**
     * Fecha de nacimiento del jugador.
     */
    private Date birthdate;

    /**
     * Valor de mercado del jugador en euros (aproximado).
     */
    private String marketValue;

    /**
     * Mano dominante del jugador (por ejemplo, "derecha" o "izquierda").
     */
    private String hand;

    /**
     * Lado del campo en el que juega el jugador (por ejemplo, "derecha" o "izquierda").
     */
    private String side;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Jugador() {}

    /**
     * Constructor para inicializar todos los atributos del jugador.
     *
     * @param id           El identificador único del jugador.
     * @param name         El nombre del jugador.
     * @param url          La URL de información del jugador.
     * @param ranking      El ranking del jugador.
     * @param points       Los puntos acumulados por el jugador.
     * @param height       La altura del jugador.
     * @param nationality  La nacionalidad del jugador.
     * @param birthplace   El lugar de nacimiento del jugador.
     * @param birthdate    La fecha de nacimiento del jugador.
     * @param marketValue  El valor de mercado del jugador.
     * @param hand         La mano dominante del jugador.
     * @param side         El lado de la cancha en el que juega el jugador.
     */
    public Jugador(Integer id, String name, String url, Integer ranking, Integer points, Integer height, String nationality, String birthplace, Date birthdate, String marketValue, String hand, String side) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.ranking = ranking;
        this.points = points;
        this.height = height;
        this.nationality = nationality;
        this.birthplace = birthplace;
        this.birthdate = birthdate;
        this.marketValue = marketValue;
        this.hand = hand;
        this.side = side;
    }

    // Getters y Setters para cada atributo


}

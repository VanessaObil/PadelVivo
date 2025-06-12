-----------------------------------------TECNOLOGIAS UTILIZADAS----------------------------------

Bases de datos:

MariaDB: Es una buena base de datos para guardar la información principal de los partidos, los jugadores y los torneos de forma ordenada. (Para información estática).
MongoDB: Es una base de datos muy rápida para actualizar la información que cambia contantemente.

-----------------------------------------BASE DE DATOS-------------------------------------------
Base de datos en MARIADB:

 CREATE DATABASE PadelVivo;
 USE PadelVivo;

-- Tabla Temporadas (Seasons)
CREATE TABLE Seasons (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    start_date DATE,
    end_date DATE,
    year YEAR
);

-- Tabla Torneos (Tournaments)
CREATE TABLE Tournaments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    location VARCHAR(255),
    country VARCHAR(100),
    level VARCHAR(50),
    status VARCHAR(50),
    start_date DATE,
    end_date DATE,
    season_id INT, -- Clave foránea con Seasons
    FOREIGN KEY (season_id) REFERENCES Seasons(id) ON DELETE CASCADE
);

-- Tabla Jugadores (Players)
CREATE TABLE Players (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    url VARCHAR(255),
    ranking INT,
    points INT,
    height INT,
    nationality VARCHAR(100),
    birthplace VARCHAR(255),
    birthdate DATE,
    market_value VARCHAR(50),
    hand VARCHAR(10),
    side VARCHAR(10)

);

-- Tabla Partidos (Matches)
CREATE TABLE Matches (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    resultado_final VARCHAR(50),
    fecha_partido DATETIME,
    tournament_id INT, -- Clave foránea con Tournaments
    id_jugador1 INT,
    id_jugador2 INT,
    id_jugador3 INT,
    id_jugador4 INT,
    FOREIGN KEY (tournament_id) REFERENCES Tournaments(id) ON DELETE CASCADE,
    FOREIGN KEY (id_jugador1) REFERENCES Players(id) ON DELETE CASCADE,
    FOREIGN KEY (id_jugador2) REFERENCES Players(id) ON DELETE CASCADE,
    FOREIGN KEY (id_jugador3) REFERENCES Players(id) ON DELETE CASCADE,
    FOREIGN KEY (id_jugador4) REFERENCES Players(id) ON DELETE CASCADE
);

Base de datos en MONGODB:

Tabla para almacenar los datos en tiempo real en MongoDB
{
partido_id: "id_del_partido",
marcador_actual: "marcador actual",
estado_partido: "En curso",
sets_actuales: "sets",
puntos_actuales: "puntos",
jugadores_actuales: ["id_jugador1", "id_jugador2", "id_jugador3", "id_jugador4"]
}

(puede servir para las notificaciones)
Tabla para almacenar las actualizaciones en tiempo real en MongoDB
{
id_actualizacion: "id de la actualizacion",
partido_id: "id_del_partido",
tipo_actualizacion: "Punto/Set/Fin de partido",
datos_actualizacion: "datos de la actualizacion", : quien marcó el ultimo punto,
fecha_actualizacion: "fecha y hora de la actualizacion"
}

---------------------------------DESCRIPCION-------------------------------------------------

Base de datos en MariaDB:

Seasons (Temporadas): Guarda la información básica de las temporadas de pádel (ID, nombre, inicio, fin, año).

Tournaments (Torneos): Guarda los detalles de los torneos (ID, nombre, lugar, país, nivel, estado, inicio, fin) y a qué temporada pertenecen.

Players (Jugadores): Guarda la información de cada jugador (ID, nombre, página web, ranking, puntos, altura, nacionalidad, lugar y fecha de nacimiento, valor de mercado, mano con la que juega, posición en la pista).

Matches (Partidos): Guarda la información de cada partido (ID, nombre, resultado final, fecha y hora del partido) y los IDs del torneo y de los cuatro jugadores que participan.


Base de datos en MongoDB:

Tabla para datos en tiempo real: Guarda la información actual de un partido mientras se juega (ID del partido, marcador actual, estado del partido, sets actuales, puntos actuales, IDs de los jugadores actuales).

Tabla para actualizaciones en tiempo real: Guarda cada evento que ocurre durante un partido (ID de la actualización, ID del partido, tipo de actualización - punto, set, fin - detalles de la actualización, y la fecha y hora de la actualización).
package com.padel.api_padel.services;

import com.padel.api_padel.entity.Jugador;
import com.padel.api_padel.repositories.JugadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con los jugadores.
 * Este servicio utiliza el repositorio de jugadores para realizar las operaciones CRUD.
 */
@Service
public class JugadorService {

    // Repositorio para interactuar con la base de datos de jugadores
    private final JugadorRepository jugadorRepository;

    /**
     * Constructor de la clase JugadorService.
     * Se inyecta el JugadorRepository necesario para realizar las operaciones CRUD en la base de datos.
     *
     * @param jugadorRepository Repositorio de jugadores
     */
    @Autowired
    public JugadorService(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    /**
     * Método que obtiene todos los jugadores de la base de datos.
     *
     * @return Lista de jugadores
     */
    public List<Jugador> obtenerTodosLosJugadores() {
        return jugadorRepository.findAll();
    }

    /**
     * Método para eliminar un jugador por su ID.
     * Si el jugador existe en la base de datos, se elimina y retorna true.
     * Si no existe, retorna false.
     *
     * @param id El ID del jugador a eliminar
     * @return true si el jugador fue eliminado, false si no existía
     */
    public boolean eliminarJugador(Long id) {
        if (jugadorRepository.existsById(id)) {
            jugadorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Método para guardar un jugador en la base de datos.
     * Si el jugador ya existe, se actualiza; si no, se crea un nuevo registro.
     *
     * @param jugador El jugador a guardar
     */
    public void guardarJugador(Jugador jugador) {
        jugadorRepository.save(jugador);
    }

    /**
     * Método que se puede usar para guardar un jugador, y que retorna el jugador guardado.
     * Este método está comentado porque no es necesario si solo se necesita guardar sin retorno.
     *
     * @param jugador El jugador a guardar
     * @return El jugador guardado
     */
//    public Jugador guardarJugador(Jugador jugador) {
//        return jugadorRepository.save(jugador); // Guarda y retorna el jugador creado
//    }

    public Optional<Jugador> actualizarJugador(Long id, Jugador jugadorActualizado) {
        return jugadorRepository.findById(id).map(jugador -> {
            jugador.setName(jugadorActualizado.getName());
            jugador.setRanking(jugadorActualizado.getRanking());
            jugador.setPoints(jugadorActualizado.getPoints());
            jugador.setHeight(jugadorActualizado.getHeight());
            jugador.setNationality(jugadorActualizado.getNationality());
            jugador.setBirthplace(jugadorActualizado.getBirthplace());
            jugador.setBirthdate(jugadorActualizado.getBirthdate());
            jugador.setMarketValue(jugadorActualizado.getMarketValue());
            jugador.setHand(jugadorActualizado.getHand());
            jugador.setSide(jugadorActualizado.getSide());
            return jugadorRepository.save(jugador);
        });
    }
}

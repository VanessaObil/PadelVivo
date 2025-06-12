package com.padel.api_padel.services;

import com.padel.api_padel.entity.Temporada;
import com.padel.api_padel.repositories.TemporadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con las temporadas.
 * Este servicio permite obtener, guardar, actualizar y eliminar temporadas en la base de datos.
 */
@Service
public class TemporadaService {

    // Repositorio de temporadas para interactuar con la base de datos
    private final TemporadaRepository temporadaRepository;

    /**
     * Constructor de la clase TemporadaService.
     * Este constructor se encarga de inyectar las dependencias necesarias para el servicio.
     *
     * @param temporadaRepository Instancia de TemporadaRepository para interactuar con la base de datos
     */
    @Autowired
    public TemporadaService(TemporadaRepository temporadaRepository) {
        this.temporadaRepository = temporadaRepository;
    }

    /**
     * Método para obtener todas las temporadas almacenadas en la base de datos.
     *
     * @return Una lista de todas las temporadas en la base de datos
     */
    public List<Temporada> getAllTemporadas() {
        return temporadaRepository.findAll();
    }

    /**
     * Método para guardar una nueva temporada en la base de datos.
     *
     * @param temporada La temporada que se desea guardar
     */
    public void guardarTemporada(Temporada temporada) {
        temporadaRepository.save(temporada);
    }

    /**
     * Método para eliminar una temporada de la base de datos por su ID.
     *
     * @param id El ID de la temporada a eliminar
     * @return true si la temporada fue eliminada correctamente, false si no fue encontrada
     */
    public boolean eliminarTemporada(Integer id) {
        if (temporadaRepository.existsById(id)) {
            temporadaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Método para actualizar una temporada existente en la base de datos.
     *
     * @param id El ID de la temporada a actualizar
     * @param temporada La temporada con los nuevos valores a actualizar
     * @return La temporada actualizada
     * @throws RuntimeException Si la temporada con el ID especificado no es encontrada
     */
    public Temporada actualizarTemporada(Integer id, Temporada temporada) {
        Optional<Temporada> temporadaExistente = temporadaRepository.findById(id);
        if (temporadaExistente.isPresent()) {
            Temporada t = temporadaExistente.get();
            t.setName(temporada.getName());
            t.setStartDate(temporada.getStartDate());
            t.setEndDate(temporada.getEndDate());

            return temporadaRepository.save(t);
        }
        throw new RuntimeException("Temporada no encontrada");
    }
}

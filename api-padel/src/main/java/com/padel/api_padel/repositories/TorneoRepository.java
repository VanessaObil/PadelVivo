package com.padel.api_padel.repositories;

import com.padel.api_padel.entity.Temporada;
import com.padel.api_padel.entity.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TorneoRepository extends JpaRepository<Torneo, Integer> {
    List<Torneo> findByTemporada_Id(Integer temporadaId); // Buscar por ID de temporada
    List<Torneo> findByTemporada(Temporada temporada); // Buscar por temporada completa
}

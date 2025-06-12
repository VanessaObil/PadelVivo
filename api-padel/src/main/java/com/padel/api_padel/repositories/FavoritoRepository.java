package com.padel.api_padel.repositories;

import com.padel.api_padel.entity.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    List<Favorito> findByUserId(Long userId);
    Optional<Favorito> findByUserIdAndMatchId(Long userId, String matchId);
    void deleteByUserIdAndMatchId(Long userId, String matchId);
}

package com.padel.api_padel.services;

import com.padel.api_padel.entity.Favorito;
import com.padel.api_padel.repositories.FavoritoRepository;
import com.padel.api_padel.database.DatabaseManager;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoritoService {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private DatabaseManager databaseManager;

    public void agregarAFavoritos(Long userId, String matchId) {
        favoritoRepository.findByUserIdAndMatchId(userId, matchId)
                .ifPresentOrElse(
                        favorito -> {}, // ya existe, no hace nada
                        () -> {
                            Favorito favorito = new Favorito();
                            favorito.setUserId(userId);
                            favorito.setMatchId(matchId);
                            favoritoRepository.save(favorito);
                        }
                );
    }

    public void eliminarDeFavoritos(Long userId, String matchId) {
        favoritoRepository.deleteByUserIdAndMatchId(userId, matchId);
    }

    public List<Document> obtenerFavoritosPorUsuario(Long userId) {
        List<String> matchIds = favoritoRepository.findByUserId(userId)
                .stream()
                .map(Favorito::getMatchId)
                .collect(Collectors.toList());

        return databaseManager.obtenerPartidosPorIds(matchIds);
    }

    public boolean estaEnFavoritos(Long userId, String matchId) {
        return favoritoRepository.findByUserIdAndMatchId(userId, matchId).isPresent();
    }
}

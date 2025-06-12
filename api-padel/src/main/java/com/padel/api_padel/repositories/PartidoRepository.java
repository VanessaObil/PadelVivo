package com.padel.api_padel.repositories;

import com.padel.api_padel.entity.Partido;
import org.bson.Document;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PartidoRepository extends MongoRepository<Partido, String> {
    Optional<Document> findByPartidoId(String partidoId);



}
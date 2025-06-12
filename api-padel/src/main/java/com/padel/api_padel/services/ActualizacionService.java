package com.padel.api_padel.services;

import com.padel.api_padel.database.DatabaseManager;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio que maneja las operaciones relacionadas con las actualizaciones.
 * Se comunica con el DatabaseManager para obtener los datos de actualizaciones.
 */
@Service
public class ActualizacionService {

    /**
     * Instancia de DatabaseManager, que se inyecta automáticamente a través de Spring.
     * DatabaseManager maneja las interacciones con la base de datos (MariaDB y MongoDB).
     */
    private final DatabaseManager databaseManager;

    /**
     * Constructor que inyecta la dependencia de DatabaseManager.
     *
     * @param databaseManager Instancia de DatabaseManager.
     */
    @Autowired
    public ActualizacionService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Obtiene todas las actualizaciones desde la base de datos a través de DatabaseManager.
     *
     * @return Lista de documentos con las actualizaciones.
     */
    public List<Document> obtenerTodasLasActualizaciones() {
        // Se llama al método en DatabaseManager para obtener todas las actualizaciones.
        return databaseManager.obtenerTodasLasActualizaciones();
    }
}

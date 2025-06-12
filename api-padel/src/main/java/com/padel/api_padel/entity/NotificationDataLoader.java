package com.padel.api_padel.entity;


import com.padel.api_padel.repositories.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.UUID;

@Component
public class NotificationDataLoader implements CommandLineRunner {


    private static final Logger logger = LoggerFactory.getLogger(NotificationDataLoader.class);


    @Autowired
    private NotificationRepository notificationRepository;


    /**
     * Método que se ejecuta al inicio de la aplicación para insertar notificaciones de ejemplo.
     *
     * @param args argumentos de la línea de comandos
     */
    @Override
    public void run(String... args) {
        logger.info("Iniciando la carga de datos de notificaciones...");


        notificationRepository.deleteAll() // Limpia las notificaciones previas
                .thenMany(
                        Flux.just(
                                new Notification(UUID.randomUUID().toString(), "Nuevo partido", "Mira quien está jugando", false, Instant.now()),
                                new Notification(UUID.randomUUID().toString(), "Nueva temporada añadida", "Dale un vistazo", false, Instant.now()),
                                new Notification(UUID.randomUUID().toString(), "Partido terminado", "Mira como ha quedado el marcador", false, Instant.now())
                        )
                )
                .flatMap(notificationRepository::save) // Inserta las notificaciones en MongoDB
                .doOnNext(notification -> logger.info("Notificación insertada: {}", notification))
                .doOnError(error -> logger.error("Error al insertar notificación", error))
                .subscribe();
    }
}


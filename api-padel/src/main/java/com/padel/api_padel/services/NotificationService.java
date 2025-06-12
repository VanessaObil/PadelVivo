package com.padel.api_padel.services;


import com.padel.api_padel.dtos.NotificationCreateDTO;
import com.padel.api_padel.dtos.NotificationDTO;
import com.padel.api_padel.entity.Notification;
import com.padel.api_padel.mappers.NotificationMapper;
import com.padel.api_padel.repositories.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public Mono<NotificationDTO> saveNotification(NotificationCreateDTO notificationCreateDTO) {
        Notification notification = NotificationMapper.toEntity(notificationCreateDTO);

        return notificationRepository.save(notification)
                .doOnSuccess(savedNotification -> {
                    String username = SecurityContextHolder.getContext().getAuthentication().getName();

                    logger.info("🔔 Enviando notificación al usuario autenticado: {}", username);

                    messagingTemplate.convertAndSendToUser(
                            username,
                            "/notifications",
                            NotificationMapper.todTO(savedNotification)
                    );

                    logger.info("✅ Notificación enviada correctamente a /user/{}/notifications", username);
                })
                .doOnError(e -> logger.error("❌ Error al guardar o enviar notificación: ", e))
                .map(NotificationMapper::todTO);
    }

    public Flux<NotificationDTO> getAllNotifications() {
        return notificationRepository.findAll()
                .map(NotificationMapper::todTO);
    }
    public void sendNotificationToUser(String username, NotificationDTO notification) {
        messagingTemplate.convertAndSendToUser(
                username,
                "/queue/notifications",  // coincidir con lo que escucha el cliente Angular
                notification
        );
    }
}
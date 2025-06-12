package com.padel.api_padel.mappers;



import com.padel.api_padel.dtos.NotificationCreateDTO;
import com.padel.api_padel.dtos.NotificationDTO;
import com.padel.api_padel.entity.Notification;

import java.time.Instant;

public class NotificationMapper {

    public static NotificationDTO todTO (Notification notification){
        if( notification == null){
            return  null;
        }
        return  new NotificationDTO(
                notification.getId(),
                notification.getSubject(),
                notification.getMessage(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }

    public static Notification toEntity(NotificationCreateDTO notificationCreateDTO){
        if (notificationCreateDTO == null){
            return null;
        }
        return new Notification(
          null,
          notificationCreateDTO.getSubject(),
          notificationCreateDTO.getMessage(),
                notificationCreateDTO.isRead(),
                Instant.now()
        );
    }
}

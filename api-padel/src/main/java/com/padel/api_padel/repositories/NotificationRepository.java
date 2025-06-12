package com.padel.api_padel.repositories;

import com.padel.api_padel.entity.Notification;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface NotificationRepository extends ReactiveMongoRepository<Notification, String> {



}

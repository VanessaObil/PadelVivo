package com.padel.api_padel.controllers;


import com.padel.api_padel.dtos.NotificationCreateDTO;
import com.padel.api_padel.dtos.NotificationDTO;
import com.padel.api_padel.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/ws/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;


    @GetMapping
    public Flux<NotificationDTO> getAllNotifications(){
        return notificationService.getAllNotifications();
    }


    @PostMapping
    public Mono<NotificationDTO> createNotification(@RequestBody NotificationCreateDTO notificationCreateDTO){
        return notificationService.saveNotification(notificationCreateDTO);
    }



}

package com.padel.api_padel.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {

    private String id;

    private  String subject;

    private  String message;

    private boolean read;

    private Instant createdAt;

}

package com.example.service.producer;

import com.example.config.KafkaConfig;
import com.example.dto.UserNotificationEventDTO;
import com.example.entity.Action;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserEventPublisher {

    private final KafkaTemplate<String, UserNotificationEventDTO> kafkaTemplate;

    public void publishEvent(String name, String email, Action action) {
        UUID eventId = UUID.randomUUID();
        UserNotificationEventDTO event = new UserNotificationEventDTO();
        event.setEventId(UUID.randomUUID());
        event.setName(name);
        event.setEmail(email);
        event.setActionType(action);
        event.setTimestamp(LocalDateTime.now());

        // Decided to make Key: email
        kafkaTemplate.send(KafkaConfig.TOPIC, email, event);
        System.out.println("Action: [" + action + "] sent with KEY [" + email + "]");
    }

}

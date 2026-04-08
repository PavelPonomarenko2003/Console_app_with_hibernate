package com.example.service.consumer;

import com.example.config.KafkaConfig;
import com.example.dto.UserNotificationEventDTO;
import com.example.entity.ProcessedEvent;
import com.example.repository.ProcessedEventRepository;
import com.example.service.impl.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import static com.example.entity.Action.*;

@Service
@RequiredArgsConstructor
public class UserEventListener {

    private final ProcessedEventRepository processedEventRepository;
    private final NotificationService notificationService;

    // adding retry-topics if we will get some troubles with message processing, we will try again, I'll make 3 attempts
    // Actually I would like to provide DTL as well
    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 2000),
            autoCreateTopics = "true",
            retryTopicSuffix = "-retry", // need just suffix in here
            dltStrategy = DltStrategy.FAIL_ON_ERROR
    )
    @KafkaListener(topics = KafkaConfig.TOPIC, groupId = "user-events-listener")
    public void userEventHandling(UserNotificationEventDTO event) {

        System.out.println("New notification from Kafka!");

        // To prevent stress on kafka, we don't need to process duplicates
        if (processedEventRepository.existsById(event.getEventId())) {
            System.out.println("Duplicates! Пропускаем событие: " + event.getEventId());
            return;
        }

        // 2. Твоя основная логика (печать в консоль, email и т.д.)
        System.out.println("Обработка сообщения: " + event.getActionType());

        // 3. СВЯЗЬ С БАЗОЙ: Сохраняем ID, чтобы таблица не была пустой
        processedEventRepository.save(new ProcessedEvent(event.getEventId()));

        // Need to check type of our event
        if (USER_CREATED == event.getActionType()) {
            notificationService.sendWelcomeEmail(event.getName(), event.getEmail());
        }
        else if (USER_DELETED == event.getActionType()) {
            notificationService.sendGoodbyeEmail(event.getName(), event.getEmail());
        }
        else {
            System.out.println("Wierd event:  " + event.getActionType());
            throw new RuntimeException("Status: " + FAILED + " We have sent that to DTO!!!!!!");
        }

        System.out.println("Time of the event: " + event.getTimestamp());
    }

    @DltHandler
    public void handleDLT(UserNotificationEventDTO event, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        System.err.println("All attempts have been exhausted. Message from topic: " + topic + " has gone to DLT: " + event + " we couldn't handle that!\n");
    }
}

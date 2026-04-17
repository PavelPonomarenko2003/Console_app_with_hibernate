package integrational_kafka_testing;

import com.example.SpringApp;
import com.example.dto.UserNotificationEventDTO;
import com.example.entity.Action;
import com.example.service.impl.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import java.time.LocalDateTime;
import java.util.UUID;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@SpringBootTest(
        classes = com.example.SpringApp.class,
        properties = {
                "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
                "spring.kafka.consumer.auto-offset-reset=earliest",
                "spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer",
                "spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer",
                "spring.kafka.consumer.properties.spring.json.trusted.packages=com.example.dto",
                "spring.jackson.serialization.write-dates-as-timestamps=false"
        }
)
// Annotation for Creating little kafka in OS, to raise kafka layer
// PLAINTEXT - protocol in kafka to transfer text
@EmbeddedKafka(
        partitions = 1,
        topics = {
                "user-events-basic-topic",
                "user-events-basic-topic-retry",
                "user-events-basic-topic-dlt"
        },
        bootstrapServersProperty = "spring.kafka.bootstrap-servers",
        adminTimeout = 60
)
@DirtiesContext
public class NotificationIntegrationalTest {

    @Autowired
    private KafkaTemplate<String, UserNotificationEventDTO> kafkaTemplate;

    @MockitoSpyBean // help to follow real object
    private NotificationService notificationService;

    @Test
    @DisplayName("Test to chek email sending in Kafka")
    void shouldSendEmailWhenUserCreatedEvent() {
        UserNotificationEventDTO event = new UserNotificationEventDTO(
                UUID.randomUUID(),
                "Pavel",
                "testing@mail.ru",
                Action.USER_CREATED,
                LocalDateTime.now()
        );

        // Sending to kafka template
        kafkaTemplate.send("user-events-basic-topic", event.getEmail(), event);
        kafkaTemplate.flush();
        // 3. Checking that NotificationService call method sendWelcomeEmail
        // Using Awaitility
        await().atMost(10, SECONDS).untilAsserted(() -> {
            verify(notificationService, atLeastOnce()).sendWelcomeEmail(any(), any());
        });
    }
}

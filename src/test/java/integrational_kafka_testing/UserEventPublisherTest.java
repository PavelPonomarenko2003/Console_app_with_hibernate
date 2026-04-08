package integrational_kafka_testing;

import com.example.SpringApp;
import com.example.config.KafkaConfig;
import com.example.dto.UserNotificationEventDTO;
import com.example.entity.Action;
import com.example.service.producer.UserEventPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest(classes = {SpringApp.class, KafkaConfig.class})
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class UserEventPublisherTest {

    @Autowired
    private UserEventPublisher publisher;

    @MockitoSpyBean
    private KafkaTemplate<String, UserNotificationEventDTO> kafkaTemplate;

    @Test
    void shouldPublishEvent() {
        publisher.publishEvent("Pavel", "test@mail.ru", Action.USER_CREATED);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            verify(kafkaTemplate).send(any(), any(), any());
        });
    }
}
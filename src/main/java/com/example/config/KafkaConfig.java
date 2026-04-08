package com.example.config;

import com.example.dto.UserNotificationEventDTO;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    // General topic for producer and consumer
    public static final String TOPIC = "user-events-basic-topic";

    @Bean
    public NewTopic userTopic() {
        return TopicBuilder.name(TOPIC)
                .partitions(3) // Our partitions for productivity
                .replicas(3)   // How many brokers will copy our data
                .build();
    }

    @Bean
    public ProducerFactory<String, UserNotificationEventDTO> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, UserNotificationEventDTO> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
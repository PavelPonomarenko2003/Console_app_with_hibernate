package com.example;

import com.example.service.producer.UserEventPublisher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringApp.class);

    }

//    @Bean
//    public CommandLineRunner testKafka(UserEventPublisher publisher) {
//        return args -> {
//            System.out.println("\n Testing topic-retry adn DTL!!!!!!!!!!!!!!!!!!1 \n");
//
//            // Отправляем сообщение, которое должно вызвать ошибку и Retry
//            publisher.publishEvent("Tester", "test@failed.com", "NOTHING");
//
//            System.out.println("\n>>>>>>> СООБЩЕНИЕ ОТПРАВЛЕНО, ЖДЕМ ОТВЕТА КОНСЬЮМЕРА... <<<<<<<\n");
//        };
//    }

}

package com.example.controller;

import com.example.entity.Action;
import com.example.service.producer.UserEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/api/test/errors")
@RequiredArgsConstructor
public class ControllerForTestingErrors {

    private final UserEventPublisher publisher;

    // I've created this method for testing how my DLT is working!
    @GetMapping("/send")
    public String sendTest() {
        UUID eventId = UUID.randomUUID();
        publisher.publishEvent("Testing test-retry and DLT!!!", "testing@fail.com", Action.FAILED);
        return "Failed message has sent to Kafka!";
    }


}

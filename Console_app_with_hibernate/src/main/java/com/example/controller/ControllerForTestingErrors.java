package com.example.controller;

import com.example.entity.Action;
import com.example.service.producer.UserEventPublisher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/api/test/errors")
@RequiredArgsConstructor
@Tag(name = "Testing errors", description = "Endpoints for testing application error handling and Kafka DLT logic")
public class ControllerForTestingErrors {

    private final UserEventPublisher publisher;

    @Operation(
            summary = "Starting Kafka DLT test",
            description = "Sends a message that is designed to fail, triggering the retry mechanism and Dead Letter Topic (DLT)."
    )
    @ApiResponse(responseCode = "200", description = "Test message successfully sent to Kafka")
    @GetMapping("/send")
    // I've created this method for testing how my DLT is working!
    public String sendTest() {
        UUID eventId = UUID.randomUUID();
        publisher.publishEvent("Testing test-retry and DLT!!!", "testing@fail.com", Action.FAILED);
        return "Failed message has sent to Kafka!";
    }
}
package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

// Such entity has been created for idempotency, to control events.
@Entity
@Table(name = "processed_events")
@Data
public class ProcessedEvent {
    @Id
    private UUID eventId; // Primary Key гарантирует уникальность
    private LocalDateTime processedAt = LocalDateTime.now();

    public ProcessedEvent() {}
    public ProcessedEvent(UUID eventId) { this.eventId = eventId; }
}

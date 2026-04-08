package com.example.dto;

import com.example.entity.Action;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNotificationEventDTO implements Serializable {

    private UUID eventId;
    private String name;
    private String email;
    private Action actionType;
    private LocalDateTime timestamp;

}

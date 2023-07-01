package com.example.core.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegistrationCanceledEvent {
    private final String userId;
    private final String reason;
}

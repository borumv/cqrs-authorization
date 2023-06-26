package com.example.core.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegistrationCancelEvent {
    private final String userId;
    private final String reason;
}

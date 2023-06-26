package com.example.core.events;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ProfileReservedEvent {
    private final String id;
    private final String userId;
    private final String nickName;
    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfRegistry;
}

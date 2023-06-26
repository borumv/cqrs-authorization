package com.example.authservice.core.events;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public final class UserRegisteredEvent {
    private String userId;
    private String email;
    private String nickName;
    private String firstName;
    private String lastName;
    private String password;
    private LocalDate dateOfRegistry;
}

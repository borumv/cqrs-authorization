package com.example.authservice.core.events;

import com.example.authservice.core.entity.RegistryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class UserRegisteredEvent {
    private String userId;
    private String email;
    private String password;
    private String nickName;
    private String firstName;
    private String lastName;
    private LocalDate dateOfRegistry;
    private RegistryStatus registryStatus;
}

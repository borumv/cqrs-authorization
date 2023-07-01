package com.example.authservice.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class AuthenticateInformation {
    private String token;
    private RegistryStatus registryStatus;
    private String reason;
}

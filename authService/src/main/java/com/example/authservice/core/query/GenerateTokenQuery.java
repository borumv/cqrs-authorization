package com.example.authservice.core.query;

import com.example.authservice.core.entity.RegistryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenerateTokenQuery {
    private String userId;
}

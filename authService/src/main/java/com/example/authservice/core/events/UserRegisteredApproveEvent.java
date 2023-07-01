package com.example.authservice.core.events;

import com.example.authservice.core.entity.RegistryStatus;
import lombok.Value;

@Value
public class UserRegisteredApproveEvent {
    String userId;
    RegistryStatus registryStatus = RegistryStatus.ACCEPTED;
}

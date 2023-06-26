package com.example.profilesservice.core.events;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ProfileCreatedEvent {

    private String id;
    private String userId;
    private String nickName;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String aboutDescription;
    private LocalDate dateOfRegistry;
}

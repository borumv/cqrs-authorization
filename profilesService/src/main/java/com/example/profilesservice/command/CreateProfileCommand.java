package com.example.profilesservice.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.LocalDate;

@Builder
@Data
public class CreateProfileCommand {
    @TargetAggregateIdentifier
    private final String id;
    private final String userId;
    private final String nickName;
    private final String firstName;
    private final String lastName;
    private final String avatarUrl;
    private final String aboutDescription;
    private final LocalDate dateOfRegistry;
}

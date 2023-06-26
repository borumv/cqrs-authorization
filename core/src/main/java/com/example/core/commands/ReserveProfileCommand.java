package com.example.core.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.LocalDate;

@Data
@Builder
public class ReserveProfileCommand {
    @TargetAggregateIdentifier
    private final String userId;
    private final String nickName;
    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfRegistry;
}

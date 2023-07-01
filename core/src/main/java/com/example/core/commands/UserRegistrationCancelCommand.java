package com.example.core.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class UserRegistrationCancelCommand {
    @TargetAggregateIdentifier
    private final String userId;
    private final String reason;
}

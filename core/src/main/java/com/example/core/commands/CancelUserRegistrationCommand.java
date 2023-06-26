package com.example.core.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class CancelUserRegistrationCommand {
    @TargetAggregateIdentifier
    private final String id;
    private final String userId;
    private final String reason;
}

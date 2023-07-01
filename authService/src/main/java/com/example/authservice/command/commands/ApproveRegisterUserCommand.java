package com.example.authservice.command.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
public class ApproveRegisterUserCommand {
    @TargetAggregateIdentifier
    private final String userId;
}

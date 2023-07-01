package com.example.authservice.command.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.LocalDate;

@Builder
@Data
public class RegisterUserCommand {
    @TargetAggregateIdentifier
    private String userId;
    private String email;
    private String password;
    private String nickName;
    private String firstName;
    private String lastName;
    private LocalDate dateOfRegistry;
}

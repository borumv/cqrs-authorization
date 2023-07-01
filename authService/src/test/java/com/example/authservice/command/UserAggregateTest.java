package com.example.authservice.command;


import com.example.authservice.command.commands.ApproveRegisterUserCommand;
import com.example.authservice.command.commands.RegisterUserCommand;
import com.example.authservice.core.entity.RegistryStatus;
import com.example.authservice.core.events.UserRegisteredApproveEvent;
import com.example.authservice.core.events.UserRegisteredEvent;
import com.example.core.commands.UserRegistrationCancelCommand;
import com.example.core.events.UserRegistrationCanceledEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith({MockitoExtension.class})
class UserAggregateTest {

    private FixtureConfiguration<UserAggregate> fixture;
    private UserRegisteredEvent userRegisteredEvent;
    private RegisterUserCommand registerUserCommand;
    private UserRegistrationCancelCommand userRegistrationCancelCommand;
    private UserRegistrationCanceledEvent userRegistrationCanceledEvent;
    private ApproveRegisterUserCommand approveRegisterUserCommand;
    private UserRegisteredApproveEvent userRegisteredApproveEvent;

    @BeforeEach
    public void setUp() {
        fixture = new AggregateTestFixture<>(UserAggregate.class);
        registerUserCommand = RegisterUserCommand.builder()
                .userId("user123")
                .nickName("john_doe")
                .email("john@example.com")
                .firstName("John")
                .lastName("Doe")
                .dateOfRegistry(LocalDate.now())
                .build();
        userRegisteredEvent = UserRegisteredEvent.builder()
                .userId(registerUserCommand.getUserId())
                .nickName(registerUserCommand.getNickName())
                .email(registerUserCommand.getEmail())
                .firstName(registerUserCommand.getFirstName())
                .lastName(registerUserCommand.getLastName())
                .dateOfRegistry(registerUserCommand.getDateOfRegistry())
                .registryStatus(RegistryStatus.WAITING)
                .build();
        userRegistrationCancelCommand = UserRegistrationCancelCommand
                .builder()
                .userId("user123")
                .reason("Cancellation reason")
                .build();
        userRegistrationCanceledEvent = UserRegistrationCanceledEvent.builder()
                .userId(userRegistrationCancelCommand.getUserId())
                .reason(userRegistrationCancelCommand.getReason())
                .build();
        approveRegisterUserCommand = new ApproveRegisterUserCommand("user123");
        userRegisteredApproveEvent = new UserRegisteredApproveEvent("user123");
    }
    @Test
    public void testFirstFixture() {
        fixture.givenNoPriorActivity()
                .when(registerUserCommand)
                .expectEvents(userRegisteredEvent);
    }
    @Test
    public void testCancelUserRegistrationCommand() {
        fixture.given(userRegisteredEvent)
                .when(userRegistrationCancelCommand)
                .expectEvents(userRegistrationCanceledEvent);
    }
    @Test
    public void testApproveRegisterUserCommand() {
        fixture.given(userRegisteredEvent)
                .when(approveRegisterUserCommand)
                .expectEvents(userRegisteredApproveEvent);
    }
}
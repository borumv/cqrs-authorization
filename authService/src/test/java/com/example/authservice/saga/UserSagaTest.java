package com.example.authservice.saga;

import com.example.authservice.command.commands.ApproveRegisterUserCommand;
import com.example.authservice.command.commands.RegisterUserCommand;
import com.example.authservice.core.entity.AuthenticateInformation;
import com.example.authservice.core.entity.RegistryStatus;
import com.example.authservice.core.events.UserRegisteredApproveEvent;
import com.example.authservice.core.events.UserRegisteredEvent;
import com.example.authservice.core.query.GenerateTokenQuery;
import com.example.core.commands.UserRegistrationCancelCommand;
import com.example.core.commands.ProfileReserveCommand;
import com.example.core.events.ProfileReservedEvent;
import com.example.core.events.UserRegistrationCanceledEvent;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.GenericCommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.MetaData;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.test.saga.SagaTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Nonnull;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.axonframework.test.matchers.Matchers.*;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.anything;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class UserSagaTest {
    private SagaTestFixture<UserRegisteredSaga> fixture;
    private static final String AGGREGATE_USER_ID = "user123";
    @MockBean
    private QueryGateway queryGateway;

    @Mock
    private QueryUpdateEmitter queryUpdateEmitter;
    @Mock
    private CommandGateway commandGateway;
    private UserRegisteredEvent userRegisteredEvent;
    private RegisterUserCommand registerUserCommand;
    private UserRegistrationCancelCommand userRegistrationCancelCommand;
    private UserRegistrationCanceledEvent userRegistrationCanceledEvent;
    private ApproveRegisterUserCommand approveRegisterUserCommand;
    private UserRegisteredApproveEvent userRegisteredApproveEvent;
    private ProfileReserveCommand profileReserveCommand;
    private ProfileReservedEvent profileReservedEvent;
    private AuthenticateInformation authenticateInformation;
    private GenerateTokenQuery generateTokenQuery;
    @Mock
    private  CommandResultMessage<? extends ProfileReservedEvent> commandResultMessage;

    @BeforeEach
    public void setUp() {
        fixture = new SagaTestFixture<>(UserRegisteredSaga.class);
        fixture.registerCommandGateway(CommandGateway.class, commandGateway);
        fixture.registerResource(commandResultMessage);
        fixture.registerResource(queryUpdateEmitter);
        registerUserCommand = RegisterUserCommand.builder()
                .userId(AGGREGATE_USER_ID)
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
                .build();
        userRegistrationCancelCommand = UserRegistrationCancelCommand
                .builder()
                .userId(AGGREGATE_USER_ID)
                .reason("Cancellation reason")
                .build();
        userRegistrationCanceledEvent = UserRegistrationCanceledEvent.builder()
                .userId(userRegistrationCancelCommand.getUserId())
                .reason(userRegistrationCancelCommand.getReason())
                .build();
        approveRegisterUserCommand = new ApproveRegisterUserCommand(AGGREGATE_USER_ID);
        userRegisteredApproveEvent = new UserRegisteredApproveEvent(AGGREGATE_USER_ID);
        profileReserveCommand = ProfileReserveCommand.builder()
                .userId(userRegisteredEvent.getUserId())
                .firstName(userRegisteredEvent.getFirstName())
                .lastName(userRegisteredEvent.getLastName())
                .nickName(userRegisteredEvent.getNickName())
                .dateOfRegistry(userRegisteredEvent.getDateOfRegistry())
                .build();
        profileReservedEvent = ProfileReservedEvent.builder().userId(AGGREGATE_USER_ID).build();
        generateTokenQuery = new GenerateTokenQuery(AGGREGATE_USER_ID);
        authenticateInformation = new AuthenticateInformation("token", RegistryStatus.ACCEPTED, "Successful registration");


    }

    @Test
    public void testHandleUserRegisteredEvent() {
        fixture.givenNoPriorActivity()
                .whenPublishingA(userRegisteredEvent)
                .expectDispatchedCommands(profileReserveCommand);
    }
    @Test
    public void testHandleUserRegisteredEventWithException() {
        fixture.givenAggregate(AGGREGATE_USER_ID).published(userRegisteredEvent)
                .whenPublishingA(userRegistrationCanceledEvent)
                .expectNoDispatchedCommands()
                .expectActiveSagas(0);
    }

    @Test
    public void testHandleProfileReservedEvent() {
        fixture.givenAggregate(AGGREGATE_USER_ID).published(userRegisteredEvent)
                .whenPublishingA(profileReservedEvent)
                .expectDispatchedCommands(approveRegisterUserCommand);
    }

    @Test
    public void testHandleUserRegisteredApproveEvent() {
        fixture.givenAggregate("user123").published(userRegisteredEvent)
                .whenPublishingA(userRegisteredApproveEvent)
                .expectNoDispatchedCommands()
                .expectActiveSagas(0);
    }
}
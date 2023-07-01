package com.example.authservice.saga;

import com.example.authservice.command.commands.ApproveRegisterUserCommand;
import com.example.authservice.core.entity.AuthenticateInformation;
import com.example.authservice.core.entity.RegistryStatus;
import com.example.authservice.core.events.UserRegisteredApproveEvent;
import com.example.authservice.core.events.UserRegisteredEvent;
import com.example.authservice.core.query.GenerateTokenQuery;
import com.example.core.commands.UserRegistrationCancelCommand;
import com.example.core.commands.ProfileReserveCommand;
import com.example.core.events.ProfileReservedEvent;
import com.example.core.events.UserRegistrationCanceledEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
@Saga
public class UserRegisteredSaga {
    @Autowired
    private transient CommandGateway commandGateway;
    @Autowired
    private transient QueryUpdateEmitter queryUpdateEmitter;

    @Autowired
    private transient QueryGateway queryGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "userId")
    public void handle(UserRegisteredEvent userRegisteredEvent) {
        // First
        ProfileReserveCommand profileReserveCommand = ProfileReserveCommand.builder()
                .userId(userRegisteredEvent.getUserId())
                .firstName(userRegisteredEvent.getFirstName())
                .lastName(userRegisteredEvent.getLastName())
                .nickName(userRegisteredEvent.getNickName())
                .dateOfRegistry(userRegisteredEvent.getDateOfRegistry())
                .build();
        log.info("Created ReserveProfileCommand in Saga with userId - " + profileReserveCommand.getUserId());
        commandGateway.send(profileReserveCommand, (commandMessage, commandResultMessage) -> {
            if (commandResultMessage.isExceptional()) {
                log.error("some error in handle UserRegisterCommand in User Saga" + commandMessage.getPayload());
                cancelUserRegistration(userRegisteredEvent, commandMessage.toString());
                log.info("call the compensation transaction cancelUserRegistration()");
            }
        });
    }
    public void cancelUserRegistration(UserRegisteredEvent profileReservedEvent, String reason) {
        UserRegistrationCancelCommand cancelProfileReservationCommand = UserRegistrationCancelCommand.builder()
                .userId(profileReservedEvent.getUserId())
                .reason(reason)
                .build();
        commandGateway.send(cancelProfileReservationCommand);
    }

    @SagaEventHandler(associationProperty = "userId")
    public void handle(ProfileReservedEvent profileReservedEvent) {
        log.info("ProfileReservedEvent was called with userId - " + profileReservedEvent.getUserId());
        ApproveRegisterUserCommand approveRegisterUserCommand =
                new ApproveRegisterUserCommand(profileReservedEvent.getUserId());
        commandGateway.send(approveRegisterUserCommand);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "userId")
    public void handle(UserRegistrationCanceledEvent event) {
        log.error("UserRegistrationCancelEvent event with userId - " + event.getUserId() +
                ". Reason - " + event.getReason());
        queryUpdateEmitter.emit(GenerateTokenQuery.class, query -> true,
                new AuthenticateInformation(null, RegistryStatus.REJECTED,
                        "Unsuccessful registration because the transaction did not reach the end"));
        SagaLifecycle.end();
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "userId")
    public void handle(UserRegisteredApproveEvent userRegisteredApproveEvent) {
        GenerateTokenQuery generateTokenQuery = new GenerateTokenQuery(userRegisteredApproveEvent.getUserId());

        queryGateway.query(generateTokenQuery, ResponseTypes.instanceOf(AuthenticateInformation.class))
                .whenComplete((tokenInfo, throwable) -> {
                    if (tokenInfo != null) {
                        log.info("UserRegisteredApproveEvent ok with userId - " + userRegisteredApproveEvent.getUserId());
                        queryUpdateEmitter.emit(GenerateTokenQuery.class, query -> true, tokenInfo);
                    } else {
                        log.error("UserRegisteredApproveEvent handle error with userId - " + userRegisteredApproveEvent.getUserId() + "Reason: - "
                        + throwable.getMessage());
                        // Обработка ошибки, если запрос не удался
                    }
                });

        SagaLifecycle.end();
    }
}


package com.example.authservice.saga;

import com.example.authservice.core.entity.Role;
import com.example.authservice.core.entity.UserCredential;
import com.example.authservice.core.events.UserRegisteredEvent;
import com.example.authservice.core.repository.UserCredentialRepository;
import com.example.core.commands.CancelUserRegistrationCommand;
import com.example.core.commands.ReserveProfileCommand;
import com.example.core.events.ProfileReservedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

@Slf4j
@Saga
public class UserSaga {
    @Autowired
    private transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "userId")
    public void handle(UserRegisteredEvent userRegisteredEvent) {
        ReserveProfileCommand reserveProfileCommand = ReserveProfileCommand.builder()
                .userId(userRegisteredEvent.getUserId())
                .firstName(userRegisteredEvent.getFirstName())
                .lastName(userRegisteredEvent.getLastName())
                .nickName(userRegisteredEvent.getNickName())
                .dateOfRegistry(userRegisteredEvent.getDateOfRegistry())
                .build();
        log.info("Created ReserveProfileCommand in Saga with userId - " + reserveProfileCommand.getUserId());
        commandGateway.send(reserveProfileCommand, (commandMessage, commandResultMessage) -> {
            if (commandResultMessage.isExceptional()) {
                log.error("some error in handle UserRegisterCommand in User Saga" + commandMessage.getPayload());
                cancelUserRegistration(userRegisteredEvent, commandMessage.toString());
                log.info("call the compensation transaction cancelUserRegistration()");
            }
        });
    }

    public void cancelUserRegistration(UserRegisteredEvent profileReservedEvent, String reason) {
        CancelUserRegistrationCommand cancelProfileReservationCommand = CancelUserRegistrationCommand.builder()
                .userId(profileReservedEvent.getUserId())
                .reason(reason)
                .build();
        commandGateway.send(cancelProfileReservationCommand);
    }

    @SagaEventHandler(associationProperty = "userId")
    public void handle(ProfileReservedEvent profileReservedEvent) {
        log.info("ProfileReservedEvent was called with userId - " + profileReservedEvent.getUserId());
    }
}

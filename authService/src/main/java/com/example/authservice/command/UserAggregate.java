package com.example.authservice.command;

import com.example.authservice.core.events.UserRegisteredEvent;
import com.example.core.commands.CancelUserRegistrationCommand;
import com.example.core.events.UserRegistrationCancelEvent;
import lombok.Data;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;

@Aggregate
@Data
public class UserAggregate {
    @AggregateIdentifier
    private String userId;
    private String email;
    private String nickName;
    private String firstName;
    private String lastName;
    private String password;
    private LocalDate dateOfRegistry;

    // Default constructor required by Axon
    public UserAggregate() {
    }

    @CommandHandler
    public UserAggregate(RegisterUserCommand registerUserCommand) {
        UserRegisteredEvent userRegisterEvent = UserRegisteredEvent.builder()
                .userId(registerUserCommand.getUserId())
                .email(registerUserCommand.getEmail())
                .nickName(registerUserCommand.getNickName())
                .firstName(registerUserCommand.getFirstName())
                .lastName(registerUserCommand.getLastName())
                .password(registerUserCommand.getPassword())
                .dateOfRegistry(registerUserCommand.getDateOfRegistry())
                .build();
        BeanUtils.copyProperties(registerUserCommand, userRegisterEvent);
        AggregateLifecycle.apply(userRegisterEvent);
    }

    @CommandHandler
    public void handle(CancelUserRegistrationCommand command){
        UserRegistrationCancelEvent userRegistrationCancelEvent = UserRegistrationCancelEvent.builder()
                .userId(command.getUserId())
                .reason(command.getReason())
                .build();
        AggregateLifecycle.apply(userRegistrationCancelEvent);
    }

//    @EventSourcingHandler - do we need?
//    public void on(UserRegistrationCancelEvent userRegistrationCancelEvent){}


    @EventSourcingHandler
    public void on(UserRegisteredEvent event) {
        this.userId = event.getUserId();
        this.email = event.getEmail();
        this.nickName = event.getNickName();
        this.firstName = event.getFirstName();
        this.lastName = event.getLastName();
        this.password = event.getPassword();
        this.dateOfRegistry = event.getDateOfRegistry();
    }

}

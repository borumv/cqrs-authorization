package com.example.authservice.command;

import com.example.authservice.command.commands.ApproveRegisterUserCommand;
import com.example.authservice.command.commands.RegisterUserCommand;
import com.example.authservice.core.entity.RegistryStatus;
import com.example.authservice.core.events.UserRegisteredApproveEvent;
import com.example.authservice.core.events.UserRegisteredEvent;
import com.example.core.commands.UserRegistrationCancelCommand;
import com.example.core.events.UserRegistrationCanceledEvent;
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
    private String nickName;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate dateOfRegistry;
    private RegistryStatus registryStatus;

    // Default constructor required by Axon
    public UserAggregate() {
    }

    @CommandHandler
    public UserAggregate(RegisterUserCommand registerUserCommand) {
        UserRegisteredEvent userRegisterEvent = UserRegisteredEvent.builder()
                .email(registerUserCommand.getEmail())
                .userId(registerUserCommand.getUserId())
                .nickName(registerUserCommand.getNickName())
                .firstName(registerUserCommand.getFirstName())
                .lastName(registerUserCommand.getLastName())
                .dateOfRegistry(registerUserCommand.getDateOfRegistry())
                .registryStatus(RegistryStatus.WAITING)
                .build();
        BeanUtils.copyProperties(registerUserCommand, userRegisterEvent);
        AggregateLifecycle.apply(userRegisterEvent);
    }

    @CommandHandler
    public void handle(UserRegistrationCancelCommand command) {
        UserRegistrationCanceledEvent userRegistrationCanceledEvent = UserRegistrationCanceledEvent.builder()
                .userId(command.getUserId())
                .reason(command.getReason())
                .build();
        AggregateLifecycle.apply(userRegistrationCanceledEvent);
    }

    @CommandHandler
    public void handle(ApproveRegisterUserCommand approveRegisterUserCommand) {
        UserRegisteredApproveEvent userRegisteredApproveEvent = new UserRegisteredApproveEvent(
                approveRegisterUserCommand.getUserId()
        );
        AggregateLifecycle.apply(userRegisteredApproveEvent);
    }

    @EventSourcingHandler
    protected void on(UserRegisteredApproveEvent userRegisteredApproveEvent) {
        this.registryStatus = userRegisteredApproveEvent.getRegistryStatus();
    }

    @EventSourcingHandler
    protected void on(UserRegistrationCanceledEvent userRegistrationCanceledEvent){
        AggregateLifecycle.markDeleted();
    }
    @EventSourcingHandler
    public void on(UserRegisteredEvent event) {
        this.email = event.getEmail();
        this.userId = event.getUserId();
        this.nickName = event.getNickName();
        this.firstName = event.getFirstName();
        this.lastName = event.getLastName();
        this.dateOfRegistry = event.getDateOfRegistry();
        this.registryStatus = event.getRegistryStatus();
    }

}

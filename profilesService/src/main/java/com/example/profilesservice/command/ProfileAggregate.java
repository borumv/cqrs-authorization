package com.example.profilesservice.command;

import com.example.core.commands.ReserveProfileCommand;
import com.example.core.events.ProfileReservedEvent;
import com.example.profilesservice.core.events.ProfileCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.util.UUID;

@Aggregate
@Slf4j
public class ProfileAggregate {


    @AggregateIdentifier
    private String id;
    private String userId;
    private String nickName;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String aboutDescription;
    private LocalDate dateOfRegistry;

    public ProfileAggregate() {
    }

    @CommandHandler
    public ProfileAggregate(CreateProfileCommand createProfileCommand) throws Exception {
        // Validate CreateProfile Command
        if (createProfileCommand.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        ProfileCreatedEvent profileCreatedEvent = new ProfileCreatedEvent();
        BeanUtils.copyProperties(createProfileCommand, profileCreatedEvent);
        AggregateLifecycle.apply(profileCreatedEvent);
    }

    @CommandHandler
    public ProfileAggregate(ReserveProfileCommand reserveProfileCommand){
        if (reserveProfileCommand.getUserId() == null) {
            throw new IllegalArgumentException("ReserveProfileCommand. User ID cannot be null");
        }
        log.info("ReserveProfileCommand accepted with id - " + reserveProfileCommand.getUserId());
        ProfileReservedEvent profileReservedEvent = ProfileReservedEvent.builder()
                .id(UUID.randomUUID().toString())
                .userId(reserveProfileCommand.getUserId())
                .firstName(reserveProfileCommand.getFirstName())
                .lastName(reserveProfileCommand.getLastName())
                .nickName(reserveProfileCommand.getNickName())
                .dateOfRegistry(reserveProfileCommand.getDateOfRegistry())
                .build();
        throw new RuntimeException("Test exception");
//        log.info("Profile reservedEvent was done. Profile id - " + profileReservedEvent.getUserId());
//        AggregateLifecycle.apply(profileReservedEvent);
//        log.info("Profile reservedEvent was sent. Profile id - " + profileReservedEvent.getUserId());
    }


    @EventSourcingHandler
    public void on(ProfileCreatedEvent profileCreatedEvent){
        this.userId = profileCreatedEvent.getUserId();
        this.aboutDescription = profileCreatedEvent.getAboutDescription();
        this.avatarUrl = profileCreatedEvent.getAvatarUrl();
        this.dateOfRegistry = profileCreatedEvent.getDateOfRegistry();
        this.firstName = profileCreatedEvent.getFirstName();
        this.lastName = profileCreatedEvent.getLastName();
        this.nickName = profileCreatedEvent.getNickName();
    }
    @EventSourcingHandler
    public void on(ProfileReservedEvent profileReservedEvent){
        this.id = profileReservedEvent.getId();
        this.userId = profileReservedEvent.getUserId();
        this.dateOfRegistry = profileReservedEvent.getDateOfRegistry();
        this.firstName = profileReservedEvent.getFirstName();
        this.lastName = profileReservedEvent.getLastName();
        this.nickName = profileReservedEvent.getNickName();
    }

}

package com.example.profilesservice.command;

import com.example.core.events.ProfileReservedEvent;
import com.example.profilesservice.core.data.ProfileLookupEntity;
import com.example.profilesservice.core.data.ProfileLookupRepository;
import com.example.profilesservice.core.events.ProfileCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("profile-group")
@Slf4j
public class ProfileLookupEventsHandler {

    ProfileLookupRepository profileLookupRepository;

    public ProfileLookupEventsHandler(ProfileLookupRepository profileLookupRepository) {
        this.profileLookupRepository = profileLookupRepository;
    }

    @EventHandler
    public void on(ProfileCreatedEvent event){
        ProfileLookupEntity profileLookupEntity = new ProfileLookupEntity(event.getUserId(), event.getNickName());
        profileLookupRepository.save(profileLookupEntity);
    }

    @EventHandler
    public void on(ProfileReservedEvent event){
        ProfileLookupEntity profileLookupEntity = new ProfileLookupEntity(event.getUserId(), event.getNickName());
        profileLookupRepository.save(profileLookupEntity);
        log.info("Saved into DB by ProfileReservedEvent with user ID - " + profileLookupEntity.getUserId());
    }
}

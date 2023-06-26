package com.example.profilesservice.query;

import com.example.core.events.ProfileReservedEvent;
import com.example.profilesservice.core.data.ProfileEntity;
import com.example.profilesservice.core.data.ProfileRepository;
import com.example.profilesservice.core.events.ProfileCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ProcessingGroup("profile-group")
public class ProfileEventHandler {

    private final ProfileRepository profileRepository;

    public ProfileEventHandler(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @ExceptionHandler(resultType = Exception.class)
    public void handle(Exception ex) throws Exception {
        throw ex;
    }
    @ExceptionHandler(resultType = IllegalArgumentException.class)
    public void handle(IllegalArgumentException ex){
        //ToDo
    }
    @EventHandler
    public void on(ProfileCreatedEvent profileCreatedEvent) throws Exception {
        ProfileEntity profileEntity = new ProfileEntity();
        BeanUtils.copyProperties(profileCreatedEvent, profileEntity);
        profileRepository.save(profileEntity);
    }

    @EventHandler
    public void on(ProfileReservedEvent profileReservedEvent) throws Exception {
        ProfileEntity profileEntity = new ProfileEntity();
        BeanUtils.copyProperties(profileReservedEvent, profileEntity);
        System.out.println();
        profileRepository.save(profileEntity);
        log.info("ProfileReservedEvent in EventHandler was called with userId - " + profileReservedEvent.getUserId());
    }
}

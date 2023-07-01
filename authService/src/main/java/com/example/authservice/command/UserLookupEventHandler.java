package com.example.authservice.command;

import com.example.authservice.core.entity.UserLookup;
import com.example.authservice.core.events.UserRegisteredEvent;
import com.example.authservice.core.repository.UserLookUpRepository;
import com.example.core.events.UserRegistrationCanceledEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("user-group")
public class UserLookupEventHandler {
    UserLookUpRepository userLookUpRepository;
    public UserLookupEventHandler(UserLookUpRepository userLookUpRepository) {
        this.userLookUpRepository = userLookUpRepository;
    }
    @EventHandler
    public void on(UserRegisteredEvent event) {
        UserLookup userLookup = UserLookup.builder()
                .userId(event.getUserId())
                .email(event.getEmail())
                .build();
        userLookUpRepository.save(userLookup);
    }
    @EventHandler
    public void on(UserRegistrationCanceledEvent event) {
        userLookUpRepository.deleteById(event.getUserId());
    }
}

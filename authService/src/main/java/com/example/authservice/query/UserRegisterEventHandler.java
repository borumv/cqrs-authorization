package com.example.authservice.query;


import com.example.authservice.core.entity.RegistryStatus;
import com.example.authservice.core.entity.UserCredential;
import com.example.authservice.core.events.UserRegisteredApproveEvent;
import com.example.authservice.core.events.UserRegisteredEvent;
import com.example.authservice.core.repository.UserCredentialRepository;
import com.example.core.events.UserRegistrationCanceledEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("user-group")
@Slf4j
public class UserRegisterEventHandler {

    private final UserCredentialRepository userRepository;

    public UserRegisterEventHandler(UserCredentialRepository profileRepository) {
        this.userRepository = profileRepository;
    }
    @EventHandler
    public void on(UserRegisteredEvent userRegisteredEvent) {
        log.info("UserRegisteredEvent with userId - " + userRegisteredEvent.getUserId() + ". We were delete it");
        UserCredential user = new UserCredential();
        BeanUtils.copyProperties(userRegisteredEvent, user);
        userRepository.save(user);
    }

    @EventHandler
    public void on(UserRegistrationCanceledEvent userRegistrationCanceledEvent) {
        log.info("UserRegistrationCancelEvent with userId - " + userRegistrationCanceledEvent.getUserId() + ". We were delete it");
        userRepository.deleteById(userRegistrationCanceledEvent.getUserId());
    }

    @EventHandler
    public void on(UserRegisteredApproveEvent userRegisteredApproveEvent) {
        log.info("UserRegisteredApproveEvent with userId - " + userRegisteredApproveEvent.getUserId());
        var user = userRepository.findById(userRegisteredApproveEvent.getUserId()).orElseThrow();
        user.setStatus(userRegisteredApproveEvent.getRegistryStatus());
        userRepository.save(user);
    }
}

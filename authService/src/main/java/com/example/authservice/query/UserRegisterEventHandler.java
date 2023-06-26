package com.example.authservice.query;


import com.example.authservice.core.entity.Role;
import com.example.authservice.core.entity.UserCredential;
import com.example.authservice.core.events.UserRegisteredEvent;
import com.example.authservice.core.repository.UserCredentialRepository;
import com.example.core.events.UserRegistrationCancelEvent;
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

//    @ExceptionHandler(resultType = Exception.class)
//    public void handle(Exception ex) throws Exception {
//        throw ex;
//    }
//    @ExceptionHandler(resultType = IllegalArgumentException.class)
//    public void handle(IllegalArgumentException ex){
//        //ToDo
//    }
//    @EventHandler
//    public void on(UserRegisteredEvent userRegisteredEvent) throws Exception {
//        UserCredential profileEntity = new UserCredential();
//        BeanUtils.copyProperties(userRegisteredEvent, profileEntity);
//        profileEntity.setRole(Role.USER);
//        userRepository.save(profileEntity);
//    }

    @EventHandler
    public void on(UserRegistrationCancelEvent userRegistrationCancelEvent) throws Exception {
        log.info("UserRegistrationCancelEvent with userId - " + userRegistrationCancelEvent.getUserId());
        userRepository.deleteById(userRegistrationCancelEvent.getUserId());
    }
}

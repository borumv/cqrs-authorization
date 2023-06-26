package com.example.authservice.command.rest.service;

import com.example.authservice.command.CreateUserRestModel;
import com.example.authservice.command.RegisterUserCommand;
import com.example.authservice.core.dto.AuthenticationResponse;
import com.example.authservice.core.repository.UserCredentialRepository;
import com.example.authservice.core.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Slf4j
public class RegisterUserService {

    private final CommandGateway commandGateway;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserCredentialRepository userCredentialRepository;

    public RegisterUserService(CommandGateway commandGateway, PasswordEncoder passwordEncoder, JwtService jwtService, UserCredentialRepository userCredentialRepository) {
        this.commandGateway = commandGateway;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userCredentialRepository = userCredentialRepository;
    }

    public AuthenticationResponse register(CreateUserRestModel registerUserRestModel) throws InterruptedException {
        RegisterUserCommand registerUserCommand = RegisterUserCommand.builder()
                .userId(UUID.randomUUID().toString())
                .firstName(registerUserRestModel.getFirstName())
                .lastName(registerUserRestModel.getLastName())
                .nickName(registerUserRestModel.getNickName())
                .email(registerUserRestModel.getEmail())
                .password(passwordEncoder.encode(registerUserRestModel.getPassword()))
                .dateOfRegistry(LocalDate.now())
                .build();
        System.out.println();
        var id = commandGateway.sendAndWait(registerUserCommand);
        Thread.sleep(100);
//        while (userCredentialRepository.findById((String) id).isEmpty()){
//            log.info(String.format("waiting a data with email %s", id));
//        }
        var registeredUser = userCredentialRepository.findById((String) id).orElseThrow(() -> new RuntimeException("Incorrect persisting"));
        var token = jwtService.generateToken(registeredUser);
        System.out.println();
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

//    public void register(CreateUserRestModel registerUserRestModel) throws InterruptedException {
//        RegisterUserCommand registerUserCommand = RegisterUserCommand.builder()
//                .userId(UUID.randomUUID().toString())
//                .firstName(registerUserRestModel.getFirstName())
//                .lastName(registerUserRestModel.getLastName())
//                .nickName(registerUserRestModel.getNickName())
//                .email(registerUserRestModel.getEmail())
//                .password(passwordEncoder.encode(registerUserRestModel.getPassword()))
//                .dateOfRegistry(LocalDate.now())
//                .build();
//        System.out.println();
//        commandGateway.sendAndWait(registerUserCommand);
//
//    }
}

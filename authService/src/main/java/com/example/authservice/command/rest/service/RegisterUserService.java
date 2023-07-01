package com.example.authservice.command.rest.service;

import com.example.authservice.command.rest.CreateUserRestModel;
import com.example.authservice.command.commands.RegisterUserCommand;
import com.example.authservice.core.dto.AuthenticationResponse;
import com.example.authservice.core.entity.*;
import com.example.authservice.core.query.GenerateTokenQuery;
import com.example.authservice.core.repository.UserCredentialRepository;
import com.example.authservice.core.repository.UserLookUpRepository;
import com.example.authservice.core.service.JwtService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j

public class RegisterUserService {
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private final PasswordEncoder passwordEncoder;
    private final UserCredentialRepository userCredentialRepository;
    private final UserLookUpRepository userLookUpRepository;


    public RegisterUserService(CommandGateway commandGateway, QueryGateway queryGateway, PasswordEncoder passwordEncoder, JwtService jwtService, UserCredentialRepository userCredentialRepository, UserLookUpRepository userLookUpRepository) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
        this.passwordEncoder = passwordEncoder;
        this.userCredentialRepository = userCredentialRepository;
        this.userLookUpRepository = userLookUpRepository;
    }
    public AuthenticateInformation register(CreateUserRestModel registerUserRestModel) {
        isExist(registerUserRestModel);
        var user = UserCredential.builder()
                .userId(generateId())
                .email(registerUserRestModel.getEmail())
                .firstName(registerUserRestModel.getFirstName())
                .lastName(registerUserRestModel.getLastName())
                .nickName(registerUserRestModel.getNickName())
                .password(passwordEncoder.encode(registerUserRestModel.getPassword()))
                .role(Role.USER)
                .build();
      //  var savedUser = userCredentialRepository.save(user);
        return generateAuthenticationToken(user);
    }
    private String generateId() {
        return UUID.randomUUID().toString();
    }
    private void isExist(CreateUserRestModel registerUserRestModel) {
        UserLookup profileLookupEntity = userLookUpRepository.findByEmail(registerUserRestModel.getEmail());
        if (profileLookupEntity != null) {
            throw new IllegalStateException(String.format("Profile with email %s already exist",
                    profileLookupEntity.getEmail()));
        }
    }
    private AuthenticateInformation generateAuthenticationToken(UserCredential savedUser) {
        sendRegisterUserCommand(savedUser);
        try (SubscriptionQueryResult<AuthenticateInformation, AuthenticateInformation> queryResult =
                     queryGateway.subscriptionQuery(
                             new GenerateTokenQuery(savedUser.getUserId()),
                             ResponseTypes.instanceOf(AuthenticateInformation.class),
                             ResponseTypes.instanceOf(AuthenticateInformation.class))) {

            return queryResult.updates().blockFirst();
        }
    }
    private void sendRegisterUserCommand(UserCredential savedUser) {
        RegisterUserCommand registerUserCommand = RegisterUserCommand.builder()
                .userId(savedUser.getUserId())
                .email(savedUser.getEmail())
                .password(savedUser.getPassword())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .nickName(savedUser.getNickName())
                .build();
        commandGateway.sendAndWait(registerUserCommand);
    }
}

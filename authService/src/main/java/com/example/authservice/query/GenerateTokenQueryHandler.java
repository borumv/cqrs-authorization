package com.example.authservice.query;

import com.example.authservice.core.entity.AuthenticateInformation;
import com.example.authservice.core.entity.RegistryStatus;
import com.example.authservice.core.query.GenerateTokenQuery;
import com.example.authservice.core.repository.UserCredentialRepository;
import com.example.authservice.core.service.JwtService;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GenerateTokenQueryHandler {


    UserCredentialRepository userCredentialRepository;
    JwtService jwtService;

    @Autowired
    public GenerateTokenQueryHandler(UserCredentialRepository userCredentialRepository, JwtService jwtService) {
        this.userCredentialRepository = userCredentialRepository;
        this.jwtService = jwtService;
    }


    @QueryHandler
    @Transactional
    public AuthenticateInformation generateToken(GenerateTokenQuery generateTokenQuery){
        var user = userCredentialRepository.findById(generateTokenQuery.getUserId()).orElseThrow(() ->
                new IllegalStateException("User haven't been registry yet"));

        var token = jwtService.generateToken(user);
        return AuthenticateInformation.builder()
                .token(token)
                .registryStatus(RegistryStatus.ACCEPTED)
                .reason("Successful registration")
                .build();
    }
}

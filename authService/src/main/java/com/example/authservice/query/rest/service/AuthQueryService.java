package com.example.authservice.query.rest.service;

import com.example.authservice.core.dto.AuthenticationResponse;
import com.example.authservice.core.dto.AuthenticationRequest;
import com.example.authservice.core.repository.UserCredentialRepository;
import com.example.authservice.core.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthQueryService {
    public AuthQueryService(UserCredentialRepository credentialRepository,
                            JwtService jwtService,
                            AuthenticationManager authenticationManager) {
        this.credentialRepository = credentialRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }
    private UserCredentialRepository credentialRepository;
    private JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var user = credentialRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var token = jwtService.generateToken(user);
        System.out.println();
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }
}

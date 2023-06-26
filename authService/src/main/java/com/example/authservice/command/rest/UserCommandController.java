package com.example.authservice.command.rest;

import com.example.authservice.command.CreateUserRestModel;
import com.example.authservice.command.rest.service.RegisterUserService;
import com.example.authservice.core.dto.AuthenticationResponse;
import com.example.authservice.core.repository.UserCredentialRepository;
import jakarta.validation.Valid;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserCommandController {
    final Environment env;
    private final RegisterUserService registerUserService;

    public UserCommandController(Environment env, RegisterUserService registerUserService, UserCredentialRepository userCredentialRepository) {
        this.env = env;
        this.registerUserService = registerUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> createProfile(@RequestBody @Valid CreateUserRestModel registerUserRestModel) throws InterruptedException {
        return ResponseEntity.ok(registerUserService.register(registerUserRestModel));
    }
}

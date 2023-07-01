package com.example.authservice.command.rest;

import com.example.authservice.command.rest.service.RegisterUserService;
import com.example.authservice.core.dto.AuthenticationResponse;
import com.example.authservice.core.entity.AuthenticateInformation;
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
    private final RegisterUserService registerUserService;

    public UserCommandController(RegisterUserService registerUserService) {
       this.registerUserService = registerUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticateInformation> createProfile(@RequestBody @Valid CreateUserRestModel registerUserRestModel) throws InterruptedException {

        return ResponseEntity.ok(registerUserService.register(registerUserRestModel));
    }
}

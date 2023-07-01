package com.example.authservice.command.rest;

import com.example.authservice.command.rest.service.RegisterUserService;
import com.example.authservice.core.entity.AuthenticateInformation;
import com.example.authservice.core.entity.RegistryStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class UserCommandControllerTest {
    @Mock
    private RegisterUserService registerUserService;

    @InjectMocks
    private UserCommandController userCommandController;

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void createProfileValidRequestReturnsAuthenticateInformation() throws InterruptedException {
        // Arrange
        CreateUserRestModel registerUserRestModel = CreateUserRestModel.builder()
                .email("username")
                .password("123")
                .build();
        AuthenticateInformation expectedInformation = new AuthenticateInformation("toen", RegistryStatus.ACCEPTED, "reason");

        when(registerUserService.register(registerUserRestModel)).thenReturn(expectedInformation);

        // Act
        ResponseEntity<AuthenticateInformation> response = userCommandController.createProfile(registerUserRestModel);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedInformation, response.getBody());
        verify(registerUserService).register(registerUserRestModel);
    }

    @Test
    void createProfile_InvalidRequest_ReturnsBadRequest() throws InterruptedException {
        // Arrange
        CreateUserRestModel registerUserRestModel = CreateUserRestModel.builder()
                .userId("")
                .password("password").build();
        Set<ConstraintViolation<CreateUserRestModel>> violations = validator.validate(registerUserRestModel);
        // Act
        ResponseEntity<AuthenticateInformation> response = userCommandController.createProfile(registerUserRestModel);

        // Assert
        assertEquals(2, violations.size());
        verify(registerUserService, times(1)).register(registerUserRestModel);
    }
}
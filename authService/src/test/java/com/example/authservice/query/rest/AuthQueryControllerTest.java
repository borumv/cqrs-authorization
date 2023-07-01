package com.example.authservice.query.rest;

import com.example.authservice.command.rest.CreateUserRestModel;
import com.example.authservice.command.rest.UserCommandController;
import com.example.authservice.core.dto.AuthenticationRequest;
import com.example.authservice.core.dto.AuthenticationResponse;
import com.example.authservice.query.rest.service.AuthQueryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class AuthQueryControllerTest {
    @Mock
    private AuthQueryService authQueryService;

    @InjectMocks
    private AuthQueryController authQueryController;

    @InjectMocks
    private UserCommandController userCommandController;



    @Test
    void authenticateValidRequestReturnsAuthenticationResponse() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest("example@mail.com", "123");
        AuthenticationResponse expectedResponse = new AuthenticationResponse("token");

        when(authQueryService.authenticate(request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<AuthenticationResponse> response = authQueryController.authenticate(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(authQueryService).authenticate(request);
    }

    @Test
    void validateTokenValidTokenReturnsValidMessage() {
        // Arrange
        String token = "validToken";

        // Act
        String message = authQueryController.validateToken(token);

        // Assert
        assertEquals("Token is valid", message);
        verify(authQueryService).validateToken(token);
    }



}
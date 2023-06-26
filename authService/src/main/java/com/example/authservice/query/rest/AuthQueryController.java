package com.example.authservice.query.rest;

import com.example.authservice.core.dto.AuthenticationRequest;
import com.example.authservice.core.dto.AuthenticationResponse;
import com.example.authservice.query.rest.service.AuthQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthQueryController {
    private final AuthQueryService authQueryService;

    public AuthQueryController(AuthQueryService authQueryService) {
        this.authQueryService = authQueryService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authQueryService.authenticate(request));
    }


    @GetMapping("/validate")
    public String validateToken(@RequestParam String token) {
        authQueryService.validateToken(token);
        return "Token is valid";
    }


}

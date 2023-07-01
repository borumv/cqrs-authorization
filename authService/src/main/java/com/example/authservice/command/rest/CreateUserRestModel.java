package com.example.authservice.command.rest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserRestModel {
    private String userId;
    @NotBlank(message = "NickName is a required field")
    private String nickName;
    @NotBlank(message = "email is a required field")
    @Email(message="Please provide a valid email address")
    private String email;
    private String firstName;
    private String lastName;
    private String password;
}

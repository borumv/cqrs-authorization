package com.example.authservice.command;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserRestModel {
    private String userId;
    @NotBlank(message = "NickName is a required field")
    private String nickName;
    @NotBlank(message = "email is a required field")
    private String email;
    private String firstName;
    private String lastName;
    private String password;
}

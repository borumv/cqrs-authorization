package com.example.profilesservice.command.rest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class CreateProfileRestModel {
    @NotNull(message = "userId is a required field")
    private String userId;
    @NotBlank(message = "NickName is a required field")
    private String nickName;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String aboutDescription;
}

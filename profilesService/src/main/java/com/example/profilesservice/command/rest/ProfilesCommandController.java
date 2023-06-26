package com.example.profilesservice.command.rest;
import com.example.profilesservice.command.CreateProfileCommand;
import jakarta.validation.Valid;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/profiles")
public class ProfilesCommandController {
    final Environment env;
    final CommandGateway commandGateway;
    public ProfilesCommandController(Environment env, CommandGateway commandGateway) {
        this.env = env;
        this.commandGateway = commandGateway;
    }
    @PostMapping
    public String createProfile(@RequestBody @Valid CreateProfileRestModel createProfileRestModel){
        CreateProfileCommand createProfileCommand = CreateProfileCommand.builder()
                .userId(createProfileRestModel.getUserId())
                .aboutDescription(createProfileRestModel.getAboutDescription())
                .avatarUrl(createProfileRestModel.getAvatarUrl())
                .firstName(createProfileRestModel.getFirstName())
                .lastName(createProfileRestModel.getLastName())
                .nickName(createProfileRestModel.getNickName())
                .dateOfRegistry(LocalDate.now())
                .build();
        String response;
        response =Long.toString(commandGateway.sendAndWait(createProfileCommand));

        return response;
    }

}

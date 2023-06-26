package com.example.authservice.command.interceptors;

import com.example.authservice.command.RegisterUserCommand;
import com.example.authservice.core.entity.UserLookup;

import com.example.authservice.core.repository.UserLookUpRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiFunction;

@Slf4j
@Component
public class RegisterUserCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    UserLookUpRepository userLookUpRepository;

    public RegisterUserCommandInterceptor(UserLookUpRepository userLookUpRepository) {
        this.userLookUpRepository = userLookUpRepository;
    }


    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@Nonnull List<? extends CommandMessage<?>> messages) {

        return (index, command) -> {
            log.info("Interceptor command: " + command.getPayloadType());
            if (RegisterUserCommand.class.equals(command.getPayloadType())) {
                RegisterUserCommand createProfileCommand = (RegisterUserCommand) command.getPayload();
                UserLookup profileLookupEntity = userLookUpRepository.findByEmail(createProfileCommand.getEmail());
                if (profileLookupEntity != null) {
                    throw new IllegalStateException(String.format("Profile with email %s already exist",
                            profileLookupEntity.getEmail()));
                }
            }
            return command;
        };
    }
}

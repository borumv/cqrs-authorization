package com.example.profilesservice.command.interceptors;

import com.example.profilesservice.command.CreateProfileCommand;
import com.example.profilesservice.core.data.ProfileLookupEntity;
import com.example.profilesservice.core.data.ProfileLookupRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiFunction;

@Slf4j
@Component
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {
    ProfileLookupRepository profileLookupRepository;

    public CreateProductCommandInterceptor(ProfileLookupRepository profileLookupRepository) {
        this.profileLookupRepository = profileLookupRepository;
    }

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@Nonnull List<? extends CommandMessage<?>> messages) {

        return (index, command) -> {
            log.info("Interceptor command: " + command.getPayloadType());
            if (CreateProfileCommand.class.equals(command.getPayloadType())) {
                CreateProfileCommand createProfileCommand = (CreateProfileCommand) command.getPayload();
                ProfileLookupEntity profileLookupEntity = profileLookupRepository.findByNickNameOrUserId(createProfileCommand.getNickName(),
                        createProfileCommand.getUserId());
                if (profileLookupEntity != null) {
                    throw new IllegalStateException(String.format("Profile with userId %s or nickName %s already exist",
                            profileLookupEntity.getUserId(), profileLookupEntity.getNickName()));
                }
//                throw new IllegalStateException("Test error");
            }
            return command;
        };
    }
}

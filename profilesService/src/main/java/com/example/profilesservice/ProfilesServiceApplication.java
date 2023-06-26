package com.example.profilesservice;

import com.example.profilesservice.command.interceptors.CreateProductCommandInterceptor;
import com.example.profilesservice.configuration.AxonConfig;
import com.example.profilesservice.core.errorhandling.ProfileServiceEventsErrorHandler;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@EnableDiscoveryClient
@SpringBootApplication
@Import({AxonConfig.class})
public class ProfilesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProfilesServiceApplication.class, args);
    }

    @Autowired
    public void registerCreateProductCommandInterceptor(ApplicationContext context,
                                                        CommandBus commandBus){
        commandBus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor.class));
    }

    @Autowired
    public void configure(EventProcessingConfigurer config){
        config.registerListenerInvocationErrorHandler("user-group",
                conf -> new ProfileServiceEventsErrorHandler());
    }
}

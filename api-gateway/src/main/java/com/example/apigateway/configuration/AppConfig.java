package com.example.apigateway.configuration;

import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate template(){
        return new RestTemplate();
    }

//    public RouteLocatorBuilder builder(){
//        return new RouteLocatorBuilder(new ConfigurableApplicationContext() {
//        })
//    }
}

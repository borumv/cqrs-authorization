package com.example.apigateway.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RefreshScope
@Configuration
public class RouteConfiguration {

    @Autowired
    AuthenticationGlobalFilter authenticationFilter;

    @Bean
    public RouteLocator apiRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("profiles-service",p -> p
                        .path("/profiles-service/profiles/**")
                        .filters(f -> f
                                .filter(authenticationFilter) // You can add more filters here.
                                .stripPrefix(1))
                        .uri("lb://PROFILES-SERVICE"))
                .build();
    }
}

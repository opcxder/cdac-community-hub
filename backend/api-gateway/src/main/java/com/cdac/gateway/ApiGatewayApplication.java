package com.cdac.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);

    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {


        return builder.routes()
                .route("auth-service", r -> {

                    return r.path("/api/auth/**").uri("lb://AUTH-SERVICE");
                })
                .route("admin-service", r -> {

                    return r.path("/api/admin/**").uri("lb://CDAC-ADMIN-SERVICE");
                })
                .route("food-service", r -> {

                    return r.path("/api/food/**").uri("lb://FOOD-SERVICE");
                })
                .route("hostel-service", r -> {

                    return r.path("/api/hostel/**").uri("lb://HOSTEL-SERVICE");
                })
                .route("suggestion-service", r -> {

                    return r.path("/api/suggestions/**").uri("lb://SUGGESTION-SERVICE");
                })
                .build();
    }
}

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
        System.out.println("API Gateway started successfully on port 8080");
    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        System.out.println("\n🔧 ========================================");
        System.out.println("🔧 CREATING GATEWAY ROUTES");
        System.out.println("🔧 ========================================");

        return builder.routes()
                .route("auth-service", r -> {
                    System.out.println("✅ Route: /api/auth/** -> lb://AUTH-SERVICE");
                    return r.path("/api/auth/**").uri("lb://AUTH-SERVICE");
                })
                .route("admin-service", r -> {
                    System.out.println("✅ Route: /api/admin/** -> lb://CDAC-ADMIN-SERVICE");
                    return r.path("/api/admin/**").uri("lb://CDAC-ADMIN-SERVICE");
                })
                .route("food-service", r -> {
                    System.out.println("✅ Route: /api/food/** -> lb://FOOD-SERVICE");
                    return r.path("/api/food/**").uri("lb://FOOD-SERVICE");
                })
                .route("hostel-service", r -> {
                    System.out.println("✅ Route: /api/hostel/** -> lb://HOSTEL-SERVICE");
                    return r.path("/api/hostel/**").uri("lb://HOSTEL-SERVICE");
                })
                .route("suggestion-service", r -> {
                    System.out.println("✅ Route: /api/suggestions/** -> lb://SUGGESTION-SERVICE");
                    return r.path("/api/suggestions/**").uri("lb://SUGGESTION-SERVICE");
                })
                .build();
    }
}

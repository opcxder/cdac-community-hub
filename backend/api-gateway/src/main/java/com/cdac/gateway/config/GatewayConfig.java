package com.cdac.gateway.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public CommandLineRunner logRoutes(RouteLocator routeLocator) {
        return args -> {
            System.out.println("\n🔧 ========================================");
            System.out.println("🔧 GATEWAY ROUTES LOADED:");
            System.out.println("🔧 ========================================");

            routeLocator.getRoutes().subscribe(route -> {
                System.out.println("🔧 Route ID: " + route.getId());
                System.out.println("   URI: " + route.getUri());
                System.out.println("   Predicates: " + route.getPredicate());
                System.out.println("   Filters: " + route.getFilters());
                System.out.println("   ---");
            });

            System.out.println("🔧 ========================================\n");
        };
    }
}

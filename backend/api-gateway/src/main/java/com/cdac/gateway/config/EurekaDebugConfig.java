package com.cdac.gateway.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EurekaDebugConfig {

    @Bean
    public CommandLineRunner logEurekaServices(DiscoveryClient discoveryClient) {
        return args -> {
            // Wait a bit for Eureka to fully initialize
            Thread.sleep(2000);

            System.out.println("\n📡 ========================================");
            System.out.println("📡 EUREKA REGISTERED SERVICES:");
            System.out.println("📡 ========================================");

            discoveryClient.getServices().forEach(serviceName -> {
                System.out.println("📡 Service: " + serviceName);
                discoveryClient.getInstances(serviceName).forEach(instance -> {
                    System.out.println("   Instance: " + instance.getHost() + ":" + instance.getPort());
                    System.out.println("   URI: " + instance.getUri());
                });
                System.out.println("   ---");
            });

            System.out.println("📡 ========================================\n");
        };
    }
}

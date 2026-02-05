package com.cdac.admin.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;



//extra
@Configuration
public class StartupLogger {

    @Bean
    public CommandLineRunner logStartupInfo(Environment env) {
        return args -> {
            System.out.println("\n🚀 ========================================");
            System.out.println("🚀 ADMIN SERVICE STARTUP INFO");
            System.out.println("🚀 ========================================");
            System.out.println("🚀 Application Name: " + env.getProperty("spring.application.name"));
            System.out.println("🚀 Active Profile: " + env.getProperty("spring.profiles.active"));
            System.out.println("🚀 Server Port: " + env.getProperty("server.port"));
            System.out.println("🚀 Eureka URL: " + env.getProperty("eureka.client.service-url.defaultZone"));
            System.out.println("🚀 Register with Eureka: " + env.getProperty("eureka.client.register-with-eureka"));
            System.out.println("🚀 ========================================\n");
        };
    }
}

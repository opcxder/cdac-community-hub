package com.cdac.hostel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;


@Configuration
public class SwaggerConfig {


    @Bean
    public OpenAPI hostelServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hostel Service API")
                        .description("CDAC Community Hub - Hostel Management Service\n\n" +
                                "This service manages hostel listings, multi-criteria ratings, " +
                                "reviews, and Bayesian ranking algorithm for hostels near CDAC campus")
                        .version("1.0.0")
             
                       )    .addServersItem(new Server()
                        .url("http://localhost:8092")
                        .description("Development Server"))
                .addServersItem(new Server()
                        .url("http://hostel-service:8092")
                        .description("Production Server (Docker)"));
    }
}

package com.cdac.food.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI foodServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Food Service API")
                .description("API for CDAC Food Service")
                .version("v0.0.1"));
    }
}

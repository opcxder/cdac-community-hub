package com.cdac.hostel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

/**
 * Swagger/OpenAPI 3.0 Configuration for Hostel Service.
 * Provides interactive API documentation accessible at /swagger-ui.html
 * API specification available at /api-docs
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configures OpenAPI documentation for the Hostel Service.
     * Includes API metadata, contact information, and server configuration.
     *
     * @return Configured OpenAPI instance
     */
    @Bean
    public OpenAPI hostelServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hostel Service API")
                        .description("CDAC Community Hub - Hostel Management Service\n\n" +
                                "This service manages hostel listings, multi-criteria ratings, " +
                                "reviews, and Bayesian ranking algorithm for hostels near CDAC campus.\n\n" +
                                "**Key Features:**\n" +
                                "- Multi-criteria rating system (5 dimensions)\n" +
                                "- Bayesian ranking algorithm\n" +
                                "- Category management with admin approval\n" +
                                "- Image upload to Cloudinary\n" +
                                "- Review replies system")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("CDAC Community Hub Team")
                                .email("support@cdac-hub.com")
                                .url("https://cdac-hub.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .addServersItem(new Server()
                        .url("http://localhost:8092")
                        .description("Development Server"))
                .addServersItem(new Server()
                        .url("http://hostel-service:8092")
                        .description("Production Server (Docker)"));
    }
}

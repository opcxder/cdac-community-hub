package com.cdac.food.config;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration for RestTemplate bean.
 * Used for making HTTP calls to external services (Auth Service).
 * 
 * Configures:
 * - Connection timeout
 * - Read timeout
 * - Error handling
 */
@Configuration
public class RestTemplateConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(RestTemplateConfig.class);
    
    /**
     * Create and configure RestTemplate bean for inter-service communication.
     * 
     * @LoadBalanced enables Eureka service discovery - allows using service names
     * instead of hardcoded URLs (e.g., http://AUTH-SERVICE/api/users)
     * 
     * Timeouts:
     * - Connect timeout: 5 seconds
     * - Read timeout: 10 seconds
     * 
     * @return configured RestTemplate instance
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        logger.info("Configuring RestTemplate for inter-service communication");
        
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(5));  // 5 seconds connection timeout
        factory.setReadTimeout(Duration.ofSeconds(10));    // 10 seconds read timeout
        
        return new RestTemplate(factory);
    }
}

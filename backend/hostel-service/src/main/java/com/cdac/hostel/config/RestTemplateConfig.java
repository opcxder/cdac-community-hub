package com.cdac.hostel.config;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestTemplateConfig {

    private static final Logger logger = LoggerFactory.getLogger(RestTemplateConfig.class);


    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        logger.info("Configuring RestTemplate for inter-service communication");

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(30)); // 30 seconds connection timeout
        factory.setReadTimeout(Duration.ofSeconds(60)); // 60 seconds read timeout

        return new RestTemplate(factory);
    }
}

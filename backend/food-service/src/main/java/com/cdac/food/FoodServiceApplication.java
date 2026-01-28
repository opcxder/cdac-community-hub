package com.cdac.food;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.cdac.food")
public class FoodServiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(FoodServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(FoodServiceApplication.class, args);
        logger.info("Food Service started successfully on port 8086");
    }
}

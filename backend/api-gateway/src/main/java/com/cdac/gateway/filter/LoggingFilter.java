package com.cdac.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().toString();

        System.out.println("🌐 ========================================");
        System.out.println("🌐 GATEWAY REQUEST");
        System.out.println("🌐 Method: " + method);
        System.out.println("🌐 Path: " + path);
        System.out.println("🌐 Full URI: " + exchange.getRequest().getURI());

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            int statusCode = exchange.getResponse().getStatusCode() != null
                    ? exchange.getResponse().getStatusCode().value()
                    : 0;

            System.out.println("🌐 Response Status: " + statusCode);
            System.out.println("🌐 ========================================");
        }));
    }

    @Override
    public int getOrder() {
        return -1; // Execute before other filters
    }
}

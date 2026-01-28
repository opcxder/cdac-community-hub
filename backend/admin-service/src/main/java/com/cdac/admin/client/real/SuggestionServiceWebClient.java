package com.cdac.admin.client.real;

import java.time.Duration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.cdac.admin.client.SuggestionServiceClient;
import com.cdac.admin.dto.SuggestionDto;
import com.cdac.admin.exception.ResourceNotFoundException;
import com.cdac.admin.exception.ServiceUnavailableException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Profile("prod")
public class SuggestionServiceWebClient implements SuggestionServiceClient {

    private static final Logger log = LoggerFactory.getLogger(SuggestionServiceWebClient.class);
    private static final String SERVICE_NAME = "Suggestion Service";

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SuggestionServiceWebClient(
            WebClient.Builder builder,
            @Value("${suggestion.service.url}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    @Override
    public List<SuggestionDto> getSuggestions(int page, int size) {

        log.info("Calling {}: get suggestions page={}, size={}", SERVICE_NAME, page, size);

        JsonNode response = webClient.get()
            .uri(uri -> uri.path("/internal/suggestions")
                .queryParam("page", page)
                .queryParam("size", size)
                .build())
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, r -> {
                if (r.statusCode() == HttpStatus.NOT_FOUND) {
                    return r.bodyToMono(String.class)
                        .map(body -> new ResourceNotFoundException(
                            SERVICE_NAME + ": suggestions not found - " + body));
                }
                return r.bodyToMono(String.class)
                    .map(body -> new IllegalArgumentException(
                        SERVICE_NAME + ": bad request - " + body));
            })
            .onStatus(HttpStatusCode::is5xxServerError, r ->
                r.bodyToMono(String.class)
                    .map(body -> new ServiceUnavailableException(
                        SERVICE_NAME + " unavailable - " + body))
            )
            .bodyToMono(JsonNode.class)
            .block(Duration.ofSeconds(5));

        return objectMapper.convertValue(
            response.get("content"),
            new TypeReference<List<SuggestionDto>>() {}
        );
    }

    @Override
    public List<SuggestionDto> getSuggestionsByCategory(String category) {

        log.info("Calling {}: get suggestions by category {}", SERVICE_NAME, category);

        return webClient.get()
            .uri("/internal/suggestions/category/{category}", category)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, r -> {
                if (r.statusCode() == HttpStatus.NOT_FOUND) {
                    return r.bodyToMono(String.class)
                        .map(body -> new ResourceNotFoundException(
                            SERVICE_NAME + ": category not found - " + body));
                }
                return r.bodyToMono(String.class)
                    .map(body -> new IllegalArgumentException(
                        SERVICE_NAME + ": bad request - " + body));
            })
            .onStatus(HttpStatusCode::is5xxServerError, r ->
                r.bodyToMono(String.class)
                    .map(body -> new ServiceUnavailableException(
                        SERVICE_NAME + " unavailable - " + body))
            )
            .bodyToFlux(SuggestionDto.class)
            .collectList()
            .block(Duration.ofSeconds(5));
    }
}

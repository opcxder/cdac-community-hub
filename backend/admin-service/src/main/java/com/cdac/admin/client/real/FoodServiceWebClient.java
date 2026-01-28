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

import com.cdac.admin.client.FoodServiceClient;
import com.cdac.admin.dto.PendingCategoryDto;
import com.cdac.admin.dto.PendingFoodDto;
import com.cdac.admin.dto.RejectFoodDto;
import com.cdac.admin.exception.ResourceNotFoundException;
import com.cdac.admin.exception.ServiceUnavailableException;

@Service
@Profile("prod")
public class FoodServiceWebClient implements FoodServiceClient {

	private final WebClient webClient;
	private static final Logger log = LoggerFactory.getLogger(FoodServiceWebClient.class);
	private static final String SERVICE_NAME = "Food Service";

	public FoodServiceWebClient(WebClient.Builder builder, @Value("${food.service.url}") String baseUrl) {
		this.webClient = builder.baseUrl(baseUrl).build();
	}

	@Override
	public List<PendingFoodDto> getPendingFood() {
		log.info("Calling {}:  getting  pending foods", SERVICE_NAME);
		return webClient.get().uri("/internal/foods/pending").retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, response -> {
		            if (response.statusCode() == HttpStatus.NOT_FOUND) {
		                return response.bodyToMono(String.class)
		                    .map(body -> new ResourceNotFoundException(
		                        SERVICE_NAME + ": food not found - " + body));
		            }
		            return response.bodyToMono(String.class)
		                .map(body -> new IllegalArgumentException(
		                    SERVICE_NAME + ": bad request - " + body));
		        })
		        .onStatus(HttpStatusCode::is5xxServerError, response ->
		            response.bodyToMono(String.class)
		                .map(body -> new ServiceUnavailableException(
		                    SERVICE_NAME + " unavailable - " + body))
		        ).bodyToFlux(PendingFoodDto.class).collectList().block(Duration.ofSeconds(5));
	}

	@Override
	public void approveFood(Long foodId) {
		log.info("Calling {}: approve  food id {}", SERVICE_NAME, foodId);

		webClient.post().uri("/internal/foods/{foodId}/approve", foodId).retrieve()
		.onStatus(HttpStatusCode::is4xxClientError, response -> {
            if (response.statusCode() == HttpStatus.NOT_FOUND) {
                return response.bodyToMono(String.class)
                    .map(body -> new ResourceNotFoundException(
                        SERVICE_NAME + ": food not found - " + body));
            }
            return response.bodyToMono(String.class)
                .map(body -> new IllegalArgumentException(
                    SERVICE_NAME + ": invalid approve request - " + body));
        })
        .onStatus(HttpStatusCode::is5xxServerError, response ->
            response.bodyToMono(String.class)
                .map(body -> new ServiceUnavailableException(
                    SERVICE_NAME + " unavailable - " + body))
        ).toBodilessEntity().block(Duration.ofSeconds(5));

	}

	@Override
	public void rejectFood(Long foodId, String reason) {
		log.info("Calling {}: reject food  {} with reason: {}", SERVICE_NAME, foodId, reason);

		webClient.post().uri("/internal/foods/{foodId}/reject", foodId).bodyValue(new RejectFoodDto(reason)).retrieve()
		.onStatus(HttpStatusCode::is4xxClientError, response -> {
            if (response.statusCode() == HttpStatus.NOT_FOUND) {
                return response.bodyToMono(String.class)
                    .map(body -> new ResourceNotFoundException(
                        SERVICE_NAME + ": user not found - " + body));
            }
            return response.bodyToMono(String.class)
                .map(body -> new IllegalArgumentException(
                    SERVICE_NAME + ": invalid reject request - " + body));
        })
        .onStatus(HttpStatusCode::is5xxServerError, response ->
            response.bodyToMono(String.class)
                .map(body -> new ServiceUnavailableException(
                    SERVICE_NAME + " unavailable - " + body))
        ).toBodilessEntity().block(Duration.ofSeconds(5));

	}
	
	
	
	@Override
	public List<PendingCategoryDto> getPendingCategories() {

	    log.info("Calling {}: getting pending categories", SERVICE_NAME);

	    return webClient.get()
	        .uri("/internal/categories/pending")
	        .retrieve()
	        .onStatus(HttpStatusCode::is4xxClientError, response -> {
	            if (response.statusCode() == HttpStatus.NOT_FOUND) {
	                return response.bodyToMono(String.class)
	                    .map(body -> new ResourceNotFoundException(
	                        SERVICE_NAME + ": category not found - " + body));
	            }
	            return response.bodyToMono(String.class)
	                .map(body -> new IllegalArgumentException(
	                    SERVICE_NAME + ": bad request - " + body));
	        })
	        .onStatus(HttpStatusCode::is5xxServerError, response ->
	            response.bodyToMono(String.class)
	                .map(body -> new ServiceUnavailableException(
	                    SERVICE_NAME + " unavailable - " + body))
	        )
	        .bodyToFlux(PendingCategoryDto.class)
	        .collectList()
	        .block(Duration.ofSeconds(5));
	}

	@Override
	public void approveCategory(Long categoryId) {

	    log.info("Calling {}: approve category {}", SERVICE_NAME, categoryId);

	    webClient.post()
	        .uri("/internal/categories/{id}/approve", categoryId)
	        .retrieve()
	        .onStatus(HttpStatusCode::is4xxClientError, response -> {
	            if (response.statusCode() == HttpStatus.NOT_FOUND) {
	                return response.bodyToMono(String.class)
	                    .map(body -> new ResourceNotFoundException(
	                        SERVICE_NAME + ": category not found - " + body));
	            }
	            return response.bodyToMono(String.class)
	                .map(body -> new IllegalArgumentException(
	                    SERVICE_NAME + ": invalid approve request - " + body));
	        })
	        .onStatus(HttpStatusCode::is5xxServerError, response ->
	            response.bodyToMono(String.class)
	                .map(body -> new ServiceUnavailableException(
	                    SERVICE_NAME + " unavailable - " + body))
	        )
	        .toBodilessEntity()
	        .block(Duration.ofSeconds(5));
	}

}

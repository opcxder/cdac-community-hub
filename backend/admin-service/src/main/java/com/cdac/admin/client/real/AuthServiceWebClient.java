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

import com.cdac.admin.client.AuthServiceClient;
import com.cdac.admin.dto.PendingUserDto;
import com.cdac.admin.dto.RejectUserDto;
import com.cdac.admin.exception.ResourceNotFoundException;
import com.cdac.admin.exception.ServiceUnavailableException;
	
	@Service
	@Profile("prod")
	public class AuthServiceWebClient implements AuthServiceClient {
	
		private final WebClient webClient;
		private static final Logger log = LoggerFactory.getLogger(AuthServiceWebClient.class);
		private static final String SERVICE_NAME = "AuthService";
	
		public AuthServiceWebClient(WebClient.Builder builder, @Value("${auth.service.url}") String baseUrl) {
			this.webClient = builder.baseUrl(baseUrl).build();
		}
	
		@Override
		public List<PendingUserDto> getPendingUser() {

		    log.info("Calling {}: get pending users", SERVICE_NAME);

		    return webClient.get()
		        .uri("/internal/users/pending")
		        .retrieve()
		        .onStatus(HttpStatusCode::is4xxClientError, response -> {
		            if (response.statusCode() == HttpStatus.NOT_FOUND) {
		                return response.bodyToMono(String.class)
		                    .map(body -> new ResourceNotFoundException(
		                        SERVICE_NAME + ": users not found - " + body));
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
		        .bodyToFlux(PendingUserDto.class)
		        .collectList()
		        .block(Duration.ofSeconds(5));
		}

	
		@Override
		public void approveUser(Long userId) {

		    log.info("Calling {}: approve user {}", SERVICE_NAME, userId);

		    webClient.post()
		        .uri("/internal/users/{id}/approve", userId)
		        .retrieve()
		        .onStatus(HttpStatusCode::is4xxClientError, response -> {
		            if (response.statusCode() == HttpStatus.NOT_FOUND) {
		                return response.bodyToMono(String.class)
		                    .map(body -> new ResourceNotFoundException(
		                        SERVICE_NAME + ": user not found - " + body));
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

	
		@Override
		public void rejectUser(Long userId, String message) {

		    log.info("Calling {}: reject user {} with reason {}", 
		             SERVICE_NAME, userId, message);

		    webClient.post()
		        .uri("/internal/users/{id}/reject", userId)
		        .bodyValue(new RejectUserDto(message))
		        .retrieve()
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
		        )
		        .toBodilessEntity()
		        .block(Duration.ofSeconds(5));
		}

	
	}

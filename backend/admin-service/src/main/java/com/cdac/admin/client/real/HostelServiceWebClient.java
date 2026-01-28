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

import com.cdac.admin.client.HostelServiceClient;
import com.cdac.admin.dto.PendingCategoryDto;
import com.cdac.admin.dto.PendingHostelDto;
import com.cdac.admin.dto.RejectHostelDto;
import com.cdac.admin.exception.ResourceNotFoundException;
import com.cdac.admin.exception.ServiceUnavailableException;

@Service
@Profile("prod")
public class HostelServiceWebClient implements HostelServiceClient {


	private static final Logger log = LoggerFactory.getLogger(HostelServiceWebClient.class);
	private static final String SERVICE_NAME = "Hostel Service";

	private final WebClient webClient;

	public HostelServiceWebClient(WebClient.Builder builder, @Value("${hostel.service.url}") String baseUrl) {
		this.webClient = builder.baseUrl(baseUrl).build();
	}

	@Override
	public List<PendingHostelDto> getPendingHostel() {
		log.info("Calling {} : getting pending hostels", SERVICE_NAME);
		return webClient.get().uri("/internal/hostels/pending").retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, response -> {
		            if (response.statusCode() == HttpStatus.NOT_FOUND) {
		                return response.bodyToMono(String.class)
		                    .map(body -> new ResourceNotFoundException(
		                        SERVICE_NAME + ": hostel not found - " + body));
		            }
		            return response.bodyToMono(String.class)
		                .map(body -> new IllegalArgumentException(
		                    SERVICE_NAME + ": bad request - " + body));
		        })
		        .onStatus(HttpStatusCode::is5xxServerError, response ->
		            response.bodyToMono(String.class)
		                .map(body -> new ServiceUnavailableException(
		                    SERVICE_NAME + " unavailable - " + body))
		        ).bodyToFlux(PendingHostelDto.class).collectList().block(Duration.ofSeconds(5));
	}

	@Override
	public void approveHostel(Long hostelId) {
		log.info("Calling {} : approve hostel id {}", SERVICE_NAME, hostelId);
		webClient.post().uri("/internal/hostels/{id}/approve", hostelId).retrieve()
		.onStatus(HttpStatusCode::is4xxClientError, response -> {
            if (response.statusCode() == HttpStatus.NOT_FOUND) {
                return response.bodyToMono(String.class)
                    .map(body -> new ResourceNotFoundException(
                        SERVICE_NAME + ": hostel not found - " + body));
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
	public void rejectHostel(Long hostelId, String reason) {
		log.info("Calling {}: reject {} for {}",SERVICE_NAME, hostelId, reason);
		
		webClient.post().uri("/internal/hostels/{hostelId}/reject", hostelId).bodyValue(new RejectHostelDto(reason)).retrieve()
		.onStatus(HttpStatusCode::is4xxClientError, response -> {
            if (response.statusCode() == HttpStatus.NOT_FOUND) {
                return response.bodyToMono(String.class)
                    .map(body -> new ResourceNotFoundException(
                        SERVICE_NAME + ": hostel not found - " + body));
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

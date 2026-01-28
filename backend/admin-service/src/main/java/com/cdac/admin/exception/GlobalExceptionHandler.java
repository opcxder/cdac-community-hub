package com.cdac.admin.exception;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	


	// Handle bad input
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex,
			HttpServletRequest request) {

		log.error("Bad request at endpoint: {}", request.getRequestURI(), ex);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", ex.getMessage()));
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException ex,
			HttpServletRequest request) {
		log.warn("Resource  not found at {}: {}", request.getRequestURI(), ex.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message ", ex.getMessage()));
	}

	@ExceptionHandler(ServiceUnavailableException.class)
	public ResponseEntity<Map<String, String>> handleServiceUnavailable(ServiceUnavailableException ex,
			HttpServletRequest request) {
		log.warn("Downstream  service unavailable at {}:  {}", request.getRequestURI(), ex.getMessage());

		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of("message", ex.getMessage()));
	}

	// Fallback - any other error
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> handleGenericException(Exception ex, HttpServletRequest request) {
		log.error("Unexpected error at endpoint: {}", request.getRequestURI(), ex);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Internal Server Error"));
	}

}

package com.ecommerce.shoppingcart.exception;

import com.ecommerce.shoppingcart.dto.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for REST controllers
 * Centralized error handling following Single Responsibility Principle
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<Response.ApiResponse<Void>> handleClientNotFound(ClientNotFoundException ex) {
        log.error("Client not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Response.ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(DuplicateClientException.class)
    public ResponseEntity<Response.ApiResponse<Void>> handleDuplicateClient(DuplicateClientException ex) {
        log.error("Duplicate client: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Response.ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(PricingStrategyNotFoundException.class)
    public ResponseEntity<Response.ApiResponse<Void>> handlePricingStrategyNotFound(
            PricingStrategyNotFoundException ex) {
        log.error("Pricing strategy not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(InvalidCartException.class)
    public ResponseEntity<Response.ApiResponse<Void>> handleInvalidCart(InvalidCartException ex) {
        log.error("Invalid cart: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response.ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        log.error("Validation errors: {}", errors);
        
        Response.ApiResponse<Map<String, String>> response = Response.ApiResponse.<Map<String, String>>builder()
                .success(false)
                .message("Validation failed")
                .data(errors)
                .timestamp(java.time.LocalDateTime.now())
                .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response.ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        log.error("Illegal argument: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response.ApiResponse<Void>> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.ApiResponse.error("An unexpected error occurred. Please contact support."));
    }
}
package com.ecommerce.shoppingcart.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTOs for client creation
 */
public class ClientRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateIndividualClientRequest {
        
        @NotBlank(message = "Client ID is required")
        @Size(min = 3, max = 50, message = "Client ID must be between 3 and 50 characters")
        private String clientId;
        
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
        private String firstName;
        
        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
        private String lastName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateProfessionalClientRequest {
        
        @NotBlank(message = "Client ID is required")
        @Size(min = 3, max = 50, message = "Client ID must be between 3 and 50 characters")
        private String clientId;
        
        @NotBlank(message = "Company name is required")
        @Size(min = 2, max = 200, message = "Company name must be between 2 and 200 characters")
        private String companyName;
        
        @Size(max = 50, message = "VAT number must not exceed 50 characters")
        private String vatNumber;
        
        @NotBlank(message = "Business registration number is required")
        @Size(min = 5, max = 50, message = "Business registration number must be between 5 and 50 characters")
        private String businessRegistrationNumber;
        
        @NotNull(message = "Annual revenue is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Annual revenue must be positive or zero")
        private BigDecimal annualRevenue;
    }
}
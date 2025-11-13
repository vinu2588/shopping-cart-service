package com.ecommerce.shoppingcart.dto.response;

import com.ecommerce.shoppingcart.domain.enums.ProductType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTOs for API
 */
public class Response {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartTotalResponse {
        private String clientId;
        private String clientType;
        private String clientName;
        private BigDecimal totalAmount;
        private String currency;
        private int totalItems;
        private List<LineItemResponse> lineItems;
        private LocalDateTime calculatedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LineItemResponse {
        private ProductType productType;
        private String productName;
        private int quantity;
        private BigDecimal unitPrice;
        private BigDecimal lineTotal;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ClientResponse {
        private Long id;
        private String clientId;
        private String clientType;
        private String firstName;
        private String lastName;
        private String companyName;
        private String vatNumber;
        private String businessRegistrationNumber;
        private BigDecimal annualRevenue;
        private Boolean isPremium;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;
        private LocalDateTime timestamp;

        public static <T> ApiResponse<T> success(T data) {
            return ApiResponse.<T>builder()
                    .success(true)
                    .message("Operation successful")
                    .data(data)
                    .timestamp(LocalDateTime.now())
                    .build();
        }

        public static <T> ApiResponse<T> success(String message, T data) {
            return ApiResponse.<T>builder()
                    .success(true)
                    .message(message)
                    .data(data)
                    .timestamp(LocalDateTime.now())
                    .build();
        }

        public static <T> ApiResponse<T> error(String message) {
            return ApiResponse.<T>builder()
                    .success(false)
                    .message(message)
                    .timestamp(LocalDateTime.now())
                    .build();
        }
    }
}
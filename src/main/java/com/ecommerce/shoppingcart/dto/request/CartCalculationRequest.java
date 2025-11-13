package com.ecommerce.shoppingcart.dto.request;

import com.ecommerce.shoppingcart.domain.enums.ProductType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO for cart calculation
 * Follows DTO pattern to decouple API from domain model
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartCalculationRequest {

    @NotBlank(message = "Client ID is required")
    private String clientId;

    @NotEmpty(message = "Cart items cannot be empty")
    @Valid
    private List<CartItemDto> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItemDto {
        
        @NotNull(message = "Product type is required")
        private ProductType productType;
        
        @Min(value = 1, message = "Quantity must be at least 1")
        private int quantity;
    }
}
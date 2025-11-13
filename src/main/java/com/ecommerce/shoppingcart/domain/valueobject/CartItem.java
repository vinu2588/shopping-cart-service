package com.ecommerce.shoppingcart.domain.valueobject;

import com.ecommerce.shoppingcart.domain.enums.ProductType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Value Object representing an item in the shopping cart
 * Immutable by design (using Lombok @Data for demo purposes)
 * In production, consider making this truly immutable
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @NotNull(message = "Product type is required")
    private ProductType productType;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    /**
     * Factory method for creating cart items
     */
    public static CartItem of(ProductType productType, int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
        return CartItem.builder()
                .productType(productType)
                .quantity(quantity)
                .build();
    }
}
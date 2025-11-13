package com.ecommerce.shoppingcart.domain.aggregate;

import com.ecommerce.shoppingcart.domain.entity.Client;
import com.ecommerce.shoppingcart.domain.valueobject.CartItem;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Aggregate root for Shopping Cart
 * Encapsulates business logic and invariants
 * Following DDD principles
 */
@Data
@Builder
public class ShoppingCart {

    private Client client;
    
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();

    /**
     * Add item to cart with quantity
     */
    public void addItem(CartItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Cart item cannot be null");
        }
        this.items.add(item);
    }

    /**
     * Get immutable view of cart items
     */
    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Calculate total number of items in cart
     */
    public int getTotalItemCount() {
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    /**
     * Validate cart state
     */
    public boolean isValid() {
        return client != null && !items.isEmpty();
    }
}
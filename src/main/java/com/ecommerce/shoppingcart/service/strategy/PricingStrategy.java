package com.ecommerce.shoppingcart.service.strategy;

import com.ecommerce.shoppingcart.domain.entity.Client;
import com.ecommerce.shoppingcart.domain.enums.ProductType;

import java.math.BigDecimal;

/**
 * Strategy pattern interface for pricing calculation
 * Allows different pricing strategies based on client type
 * Following Open/Closed Principle - open for extension, closed for modification
 */
public interface PricingStrategy {

    /**
     * Get price for a specific product type
     * 
     * @param productType The type of product
     * @return The price for this product
     */
    BigDecimal getPrice(ProductType productType);

    /**
     * Determine if this strategy applies to the given client
     * 
     * @param client The client to evaluate
     * @return true if this strategy should be used for the client
     */
    boolean appliesTo(Client client);
}
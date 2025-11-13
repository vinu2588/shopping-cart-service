package com.ecommerce.shoppingcart.exception;

/**
 * Exception thrown when no pricing strategy can be found for a client type
 */
public class PricingStrategyNotFoundException extends RuntimeException {
    public PricingStrategyNotFoundException(String message) {
        super(message);
    }
}



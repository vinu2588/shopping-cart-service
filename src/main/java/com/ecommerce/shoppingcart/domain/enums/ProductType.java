package com.ecommerce.shoppingcart.domain.enums;

/**
 * Enumeration representing types of products available in the system
 * Following Domain-Driven Design principles
 */
public enum ProductType {
    HIGH_END_PHONE("High-End Phone"),
    MID_RANGE_PHONE("Mid-Range Phone"),
    LAPTOP("Laptop");

    private final String displayName;

    ProductType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
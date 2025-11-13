package com.ecommerce.shoppingcart.domain.valueobject;

import com.ecommerce.shoppingcart.domain.enums.ProductType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Value object representing the calculated cart total with breakdown
 * Immutable result object
 */
@Data
@Builder
public class CartTotalResult {

    private String clientId;
    private String clientType;
    private String clientName;
    private BigDecimal totalAmount;
    private int totalItems;
    private Map<ProductType, LineItemDetail> lineItems;

    /**
     * Nested value object for line item details
     */
    @Data
    @Builder
    public static class LineItemDetail {
        private ProductType productType;
        private int quantity;
        private BigDecimal unitPrice;
        private BigDecimal lineTotal;
    }
}